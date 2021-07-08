1. idea设置项目编码为UTF-8
2. gradle项目修改项目编码为UTF-8后任然有GBK报错，在build.gradle中添加配置
    ```text
    tasks.withType(JavaCompile) {
      options.encoding = "UTF-8"
    }
    [compileJava, compileTestJava]*.options*.encoding = 'UTF-8'
    ```
3. 上面任然报GBK的错误
    ```text
    task androidJavadocs(type: Javadoc) {
        source = android.sourceSets.main.java.srcDirs
    }
    task androidJavadocsJar(type: Jar) {
        classifier = 'javadoc'
        from androidJavadocs.destinationDir
    }
    task androidSourcesJar(type: Jar) {
        classifier = 'sources'
        from android.sourceSets.main.java.srcDirs
    }
    artifacts {
        archives androidSourcesJar
        archives androidJavadocsJar
    }
    ```