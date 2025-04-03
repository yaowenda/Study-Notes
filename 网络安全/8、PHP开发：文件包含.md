include() 在错误发生后脚本继续执行

require() 在错误发生后脚本停止执行

include_once() 如果已经包含，则不再执行

require_once() 如果已经包含，则不再执行



把其他文件包含过来，方便开发



包含谁就执行谁

如果是`include 'upload.html'`;，那么页面就能显示出upload.html页面的内容

如果死`include($_GET['page']);` ，那么通过 `?page=upload.html` 就能显示出upload.html页面的内容



假如他的项目目录中有一个1.txt，内容是`<?php phpinfo()?>`

那么直接访问1.txt，访问到的是一个txt文件

如果是`?page=`1.txt，那么访问到的就是phpinfo，也就是txt文件被当做php执行



这是因为让用户自己决定包含谁



```php
<?php
    function getFileList($path) {
    $handle = opendir($path);
    while(($filename = readdir($handle))!== false){
        if
    }
}
    
```

