# 开发日志


## 特性

- 精简第三方依赖
- 尽量使用java自带的xml解析库
- 修正linux下主界面末端菜单项仍然提示能够展开的问题
- 仿冒osgi框架，能够加载依赖一致的第三方包
    - 仿冒osgi-bridge
    - 改为jigsow的模块化
- 加入资源仓库模块
- 资源仓库改为git支撑
- 采用微内核模式，所有功能模块均为插件

## 日志

- 201909: 
    - flat-kettle（全集源码编译) 
        到 thin-kettle（仅仅包含kettle主界面的最小项目) 
        到 one-kettle（将thin-kettle中的多个项目合为一个，便于重构)
    - 使用lint-graddle插件分析依赖项并进行版本统一去重等
    - 去除报表模块(删除report模块)
    - 去除公式模块(删除formule模块，删除engine下相关的trans)
    - 修正linux环境下后台不断报(添加环境变量SWT_GTK3=0可解决):
        'GLib-CRITICAL **: g_base64_encode_step: assertion 'in != NULL' failed'
    - 修正linux-swt环境下所有MenuItem仍然显示为可展开，展开后才能消除，通过修改"Action.AS_DROP_DOWN_MENU"为"Action.AS_UNSPECIFIED"
    - 防止OSGI加载失败和耗时，注释kettle-lifecycle-listeners.xml和kettle-registry-extensions.xml的内容
    - 解决swt内置浏览器报错问题
        - 参考: https://stackoverflow.com/questions/5817263/how-to-get-eclipse-swt-browser-component-running-on-ubuntu-11-04-natty-narwhal
        - Install the libwebkit package: sudo apt-get install libwebkitgtk-3.0-0
        - Install the libwebkit jni wrapper: sudo apt-get install libswt-webkit-gtk-3-jni
        - Set the DefaultType and UseWebKitGTK properties (I did it by adding the following to my eclipse.ini file):
            - -Dorg.eclipse.swt.browser.DefaultType=webkit
            - -Dorg.eclipse.swt.browser.UseWebKitGTK=true
    - 解决警告信息: ’Gtk-Message: 08:45:11.998: Failed to load module "canberra-gtk-module"‘ 问题
        - apt-get install libcanberra-gtk-module
    
