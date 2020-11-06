1. 项目目录结构
    ````text
    scripts
        - dist
        - mudules
            - hello
                - helloQuery.js
                - helloEdit.js
        - node_modules
        - sea-modules
        - package.json
        - webpack-config.js 
    ````
2. package.json配置
    ```
    {
      "name": "codeshare",
      "version": "1.0.0",
      "description": "webpack js package",
      "main": "index.js",
      "author": "yicj",
      "email": "626659321@qq.com",
      "date": "2017/03/01",
      "scripts": {
        "test": "echo \"Error: no test specified\" && exit 1",
        "build": "node_modules/.bin/webpack -p",
        "dev": "node_modules/.bin/webpack --progress --colors"
      },
      "devDependencies": {
        "css-loader": "^0.23.1",
        "extract-text-webpack-plugin": "^1.0.1",
        "file-loader": "^0.8.5",
        "html-loader": "^0.4.3",
        "path": "^0.12.7",
        "style-loader": "^0.13.1",
        "url-loader": "^0.5.7",
        "webpack": "^1.15.0"
      },
      "dependencies": {
        "babel-preset-env": "^1.7.0",
        "babel-core": "^6.0.0",
        "babel-eslint": "^7.0.0",
        "babel-loader": "^6.0.0",
        "babel-plugin-transform-runtime": "^6.0.0",
        "babel-preset-es2015": "^6.0.0",
        "babel-preset-stage-2": "^6.0.0",
        "babel-register": "^6.0.0"
      }
    }
    ```
3. webpack配置文件
    ```text  webpack.config.js
    'use strict';
    module.exports = {
    //    devtool: '#source-map',
        entry:{
            "helloQuery":"./modules/hello/helloQuery.js", 
            "helloEdit":"./modules/hello/helloEdit.js"
        }, 
        output: {
            path: __dirname + '/dist', //打包后的文件存放的地方
            filename: '[name].[hash:8].js' //打包后输出文件的文件名
        },
        module: {
            loaders: [
                {
                  test: /\.js$/,
                  loaders: [ 'babel-loader?presets[]=es2015' ],
                  exclude: /(node_modules|lib)/,include: __dirname
                },
                {test: /\.html$/, loader: 'html'}
            ]
        },
        resolve: {
          extensions: ['', '.js'],
           alias: {
               'seajs':__dirname+'/sea-modules',
               'srcjs':__dirname+'/modules',
               'jquery' 	: 'seajs/jquery/jquery.js',
                'global'  : 'seajs/global.js',
                'timeOut'  : 'srcjs/timeOut.js',
                'common'  : 'srcjs/common.js'
           }
        },
    }
    ```