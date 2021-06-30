### 下载源码资源
1. 源码依赖包含项目需要lib中的jar、tools中的gradle-5.6.4.LIFERAY-PATCHED-2-bin.zip
    ```text
    liferay-ce-portal-src-7.3.5-ga6-20200930172312275.zip
    ```
2. 下载源码liferay-portal-7.3.5-ga6.zip
### 配置并导入源码
1. 将liferay-ce-portal-src-7.3.5-ga6-20200930172312275.zip中lib复制到liferay-portal-7.3.5-ga6.zip的lib中
2. 修改settings.gradle文件,添加阿里云镜像
    ```text
    buildscript {
        apply from: "build-buildscript.gradle", to: buildscript
        repositories{
            maven { url "http://maven.aliyun.com/nexus/content/groups/public" }
        }
    }
    ```
3. 修改app.server.properties中bundle地址
    ```text
    app.server.parent.dir=D:\\install\\liferay-ce-portal-7.3.5-ga6
    ```
4. eclipse导入Existing Projects into Workspace
5. ant添加环境参数ANT_OPTS为-Xmx2560m
6. 将gradle-5.6.4.LIFERAY-PATCHED-2-bin.zip复制到```C:\Users\yicj1\.liferay\mirrors\github.com\liferay```目录中
    ```text
    liferay-ce-portal-src-7.3.5-ga6-20200930172312275.zip解压后tools中存在gradle-5.6.4.LIFERAY-PATCHED-2-bin.zip
    ```

