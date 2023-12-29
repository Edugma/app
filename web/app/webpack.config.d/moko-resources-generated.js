// noinspection JSUnnecessarySemicolon
;(function(config) {
    const path = require('path');
    const MiniCssExtractPlugin = require('mini-css-extract-plugin');

    const mokoResourcePath = path.resolve("C:\\Users\\tipap\\StudioProjects\\Edugma\\web\\app\\build\\generated\\moko\\jsMain\\ioedugmawebapp\\res");

    config.module.rules.push(
        {
            test: /\.(.*)/,
            resource: [
                path.resolve(mokoResourcePath, "files"),
                path.resolve(mokoResourcePath, "images"),
                path.resolve(mokoResourcePath, "localization"),
            ],
            type: 'asset/resource'
        }
    );
    
    config.plugins.push(new MiniCssExtractPlugin())
    config.module.rules.push(
        {
            test: /\.css$/,
            resource: [
                path.resolve(mokoResourcePath, "fonts"),
            ],
            use: ['style-loader', 'css-loader']
        }
    )

    config.module.rules.push(
        {
            test: /\.(otf|ttf)?$/,
            resource: [
                path.resolve(mokoResourcePath, "fonts"),
            ],
            type: 'asset/resource',
        }
    )
    
    config.resolve.modules.push(mokoResourcePath);
})(config);