## 一、测试程序调用API后为何会执行被注入的DLL？

注射器程序中调用了：

```
DetourCreateProcessWithDllEx(..., "hook.dll", ...);
```

此函数的作用是：

1. **创建目标进程（测试程序）时处于挂起状态（CREATE_SUSPENDED）**，此时程序主线程还未开始运行；
2. **将指定的DLL文件（hook.dll）路径传入**，内部通过写入远程线程、LoadLibrary 等方式，将 DLL 文件加载到测试程序的地址空间中；
3. **等DLL的加载完成后**，才恢复测试程序的主线程运行。

这就意味着：**测试程序还没执行自己的主函数，它的进程空间里就已经加载好了你的 DLL**，并且执行了**该 DLL 的入口函数**。



### DLL加载后执行的是什么代码？

是DllMain：

```
BOOL WINAPI DllMain(HMODULE hModule,
	DWORD ul_reason_for_call,
	LPVOID lpReserved
)
{
	switch (ul_reason_for_call)
	{
	case DLL_PROCESS_ATTACH:
	{
		DisableThreadLibraryCalls(hModule);
		InitHook();
		DetourTransactionBegin();
		DetourUpdateThread(GetCurrentThread());
		DetourAttach(&(PVOID&)OldReadFile, NewReadFile);
		DetourAttach(&(PVOID&)OldWriteFile, NewWriteFile);
		
		DetourTransactionCommit();
		break;
	}
	case DLL_THREAD_ATTACH:
	case DLL_THREAD_DETACH:
	case DLL_PROCESS_DETACH:
	{
		DetourTransactionBegin();
		DetourUpdateThread(GetCurrentThread());
		DetourDetach(&(PVOID&)OldReadFile, NewReadFile);
		DetourDetach(&(PVOID&)OldWriteFile, NewWriteFile);
		
		DetourTransactionCommit();
		break;
	}
	}
	return true;
}
```

所以，**测试程序启动时，hook.dll 会先执行自己的 DllMain 函数，并在 DLL_PROCESS_ATTACH 分支中调用 Hook 初始化逻辑（如 DetourAttach）**。



## 二、DLL中代码的执行顺序

### 1、阶段1：DLL注入 + Hook函数绑定

1、测试程序由注射器通过 `DetourCreateProcessWithDllEx` 启动，但处于挂起状态。

2、DLL 被加载到测试程序中（通过远程线程执行 `LoadLibrary("hook.dll")`）。

3、被注入的 DLL 自动执行 `DllMain()`，进入 `DLL_PROCESS_ATTACH` 分支。

4、在这个分支中，执行 Hook 初始化逻辑，例如：

```
DetourTransactionBegin();
DetourUpdateThread(GetCurrentThread());
DetourAttach(&(PVOID&)TrueCreateFileW, MyCreateFileW);
DetourTransactionCommit();
```

此时，**Detours 完成了 Hook：将目标 API（如 CreateFileW）的首地址重定向到你自定义的函数 MyCreateFileW。**

5、Hook 完成后，`DllMain()` 返回，DLL 初始化结束。

6、注射器调用 `ResumeThread()`，测试程序正式开始执行主函数。

### 2、阶段二：测试程序执行 + 触发API调用

1、测试程序运行中调用一个API，比如 `CreateFileW(...)`；

2、由于 Hook 已经生效，`CreateFileW` 的前几个字节已被 Detour 改写为：

```
jmp MyCreateFileW
```

所以该调用被重定向，直接跳转到你自定义的 `MyCreateFileW` 函数；