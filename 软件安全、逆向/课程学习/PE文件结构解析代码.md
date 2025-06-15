PETest.h:

```cpp
#pragma once
#include <stdio.h>
#include <Windows.h>

DWORD RvaToOffset(DWORD dwRva, char* buffer);

// 解析导入表的函数
void ImportTable(char* buffer);
```



PETest.cpp

```cpp

#include "PETest.h"



int main()
{
    FILE* pFile = NULL;
    char* buffer = NULL;
    int nFileLength = 0;

    // 使用宽字符字符串表示路径以配合 _wfopen_s
    const wchar_t* filePath = L"C:\\Users\\86151\\Desktop\\御剑2.exe";

    // 打开文件并检查是否成功
    errno_t err = _wfopen_s(&pFile, filePath, L"rb");
    if (err != 0 || pFile == NULL)
    {
        wprintf(L"Failed to open file: %d\n", err);
        return -1;
    }

    // 获取文件长度
    fseek(pFile, 0, SEEK_END);// 移动文件指针到文件末尾
    nFileLength = ftell(pFile); //返回文件指针位置
    if (nFileLength < 0)
    {
        perror("Failed to get file length");
        fclose(pFile);
        return -1;
    }
    rewind(pFile); // 移动文件指针到文件开头

    // 分配缓冲区并检查是否成功
    buffer = (char*)malloc(nFileLength);
    if (buffer == NULL)
    {
        perror("Failed to allocate memory");
        fclose(pFile);
        return -1;
    }

    // 读取文件内容到缓冲区
    size_t bytesRead = fread(buffer, 1, nFileLength, pFile);
    if (bytesRead != nFileLength)
    {
        perror("Failed to read file content");
        free(buffer);
        fclose(pFile);
        return -1;
    }

    fclose(pFile);

    // 解析 DOS 头部信息
    PIMAGE_DOS_HEADER ReadDosHeader = (PIMAGE_DOS_HEADER)buffer;
    printf("MS-DOS Info:\n");
    printf("MZ标志位: %x\n", ReadDosHeader->e_magic);
    printf("PE头偏移: %x\n", ReadDosHeader->e_lfanew);

    // 解析 PE 头部信息
    printf("PE Header Info: \n");
    PIMAGE_NT_HEADERS ReadNTHeaders;
    ReadNTHeaders = (PIMAGE_NT_HEADERS)(buffer + ReadDosHeader->e_lfanew);
    printf("PE标志位: %x\n", ReadNTHeaders->Signature);
    printf("运行平台：%x\n", ReadNTHeaders->FileHeader.Machine);
    printf("Image Base: % x\n", ReadNTHeaders->OptionalHeader.ImageBase);


    // 区段解析遍历
    printf("Section Header Info: \n");
    //IMAGE_FIRST_SECTION这个宏的作用是根据PE头的地址，计算出第一个节表的地址
    PIMAGE_SECTION_HEADER ReadSectionHeader = IMAGE_FIRST_SECTION(ReadNTHeaders);
    //获取 ReadNTHeaders 结构体中的 FileHeader 成员的地址
    PIMAGE_FILE_HEADER pFileHeader = &ReadNTHeaders->FileHeader;
    for (int i = 0; i < pFileHeader->NumberOfSections; i++) {
        printf("[%d] 区段名称: %s\n", i, ReadSectionHeader[i].Name);  // Name 是字符数组，可用 %s
        printf("VOffset(起始的相对虚拟地址): 0x%08X\n", ReadSectionHeader[i].VirtualAddress);
        printf("SizeOfRawData(虚拟大小): %u\n", ReadSectionHeader[i].SizeOfRawData);
        printf("PointerToRawData(节在文件中的偏移地址): 0x%08X\n", ReadSectionHeader[i].PointerToRawData);
        printf("VirtualSize(文件中的区段大小): %u\n", ReadSectionHeader[i].Misc.VirtualSize);
        printf("PointerToRelocations: 0x%08X\n", ReadSectionHeader[i].PointerToRelocations);
        printf("PointerToLinenumbers: 0x%08X\n", ReadSectionHeader[i].PointerToLinenumbers);
        printf("NumberOfRelocations: %u\n", ReadSectionHeader[i].NumberOfRelocations);
        printf("NumberOfLinenumbers: %u\n", ReadSectionHeader[i].NumberOfLinenumbers);
        printf("Characteristics(区段的属性): 0x%08X\n", ReadSectionHeader[i].Characteristics);
        printf("\n");
    }

    //_IMAGE_DATA_DIRECTORY
    //IMAGE_DIRECTORY_ENTRY_IMPORT

    printf("========================================================");
    ImportTable(buffer);

    // 释放内存
    free(buffer);
    return 0;
}

// dwRva是某个数据目录表的VirtualAddress
// buffer是读取到的PE文件缓冲区
// 函数目的：将给定的RVA转换为它在PE文件中的字节偏移
DWORD RvaToOffset(DWORD dwRva, char* buffer)
{
    // DOS头
    PIMAGE_DOS_HEADER pDOS = (PIMAGE_DOS_HEADER)buffer;
    // PE头
    PIMAGE_NT_HEADERS pNT = (PIMAGE_NT_HEADERS)(buffer + pDOS->e_lfanew);
    // 区段表
    // pSection是获取到所有区段表的第一个区段的首地址，可以通过pSection[i]访问所有区段表
    PIMAGE_SECTION_HEADER pSection = IMAGE_FIRST_SECTION(pNT); //它是通过一个宏来定位的
    // 判断是否落在了头部当中
    if (dwRva < pSection[0].VirtualAddress) {
        return dwRva;
    }
    for (int i = 0; i < pNT->FileHeader.NumberOfSections; i++) {
        // 判断是否落在了某个区段当中
        if (dwRva >= pSection[i].VirtualAddress && dwRva < pSection[i].VirtualAddress + pSection[i].Misc.VirtualSize)
        {   
            // dwRva - pSection[i].VirtualAddress是数据目录表起始地址到区段起始地址的偏移量
            // pSection[i].PointerToRawData是区段起始地址到文件起始地址的偏移量
            // 返回的是数据目录表起始地址到文件起始地址的偏移
            return dwRva - pSection[i].VirtualAddress + pSection[i].PointerToRawData;
        }
        // VirtualAddress 起始地址
        // Size 长度
        // VirtualAddress + Size 结束地址

    }
    
    return 0;
}

//解析导入表信息
void ImportTable(char* buffer)
{
    //DOS
    PIMAGE_DOS_HEADER pDos = (PIMAGE_DOS_HEADER)buffer;
    //PE
    PIMAGE_NT_HEADERS pNt = (PIMAGE_NT_HEADERS)(pDos->e_lfanew + buffer);
    //定位导入表
    PIMAGE_DATA_DIRECTORY pImportDir = (PIMAGE_DATA_DIRECTORY)(pNt->OptionalHeader.DataDirectory + IMAGE_DIRECTORY_ENTRY_IMPORT);
    // 填充结构
    PIMAGE_IMPORT_DESCRIPTOR pImport = (PIMAGE_IMPORT_DESCRIPTOR)(RvaToOffset(pImportDir->VirtualAddress, buffer) + buffer);
    while (pImport->Name != NULL) 
    {
        char szDllName = (char)(RvaToOffset(pImport->Name, buffer) + buffer);
        printf("DLL名称：%s\n", szDllName);
        printf("日期时间标志：%08x\n", pImport->TimeDateStamp);
        printf("ForwarderChain: %08x\n", pImport->ForwarderChain);
        printf("名称offset: %08x\n", pImport->Name);
        printf("FirstThunk: %08x\n", pImport->FirstThunk);
        printf("OriginalFirstThunk: %08x\n", pImport->OriginalFirstThunk);
        printf("\n");

        //解析PE文件中导入函数名称
        
        // 指向导入函数地址表的RVA
        PIMAGE_THUNK_DATA pIat = (PIMAGE_THUNK_DATA)(RvaToOffset(pImport->OriginalFirstThunk, buffer) + buffer);
        DWORD index = 0;
        DWORD ImportOffset = 0;
        //被导入函数的序号
        while (pIat->u1.Ordinal != 0)
        {
            printf("ThunkRva: %08x\n", pImport->OriginalFirstThunk + index);
            ImportOffset = RvaToOffset(pImport->OriginalFirstThunk, buffer);
            printf("ThunkOffset: %08x\n", ImportOffset + index);
            index += 4;
            if ((pIat->u1.Ordinal & 0x80000000) != 1)
            {
                PIMAGE_IMPORT_BY_NAME pName = (PIMAGE_IMPORT_BY_NAME)(RvaToOffset(pIat->u1.AddressOfData, buffer) + buffer);
                printf("API名称：%s\n", pName->Name);
                printf("序号Hint：%04x\n", pName->Hint);
                printf("ThunkValue: %08x\n", pIat->u1.Function);

            }
            pIat++;
        }
        
        
        
        pImport++;
    }
}




```

