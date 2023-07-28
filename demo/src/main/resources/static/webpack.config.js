const path = require('path');

module.exports = {
    entry: './js/index.js',
    output: {
        filename: 'main.js',
        path: path.resolve(__dirname, 'dist'),
        library: "common"
    },
    resolve:{
        extensions:['.js','.css','.sass']
    },
    module: {
        rules: [
            {
                test: /\.(?:js|mjs|cjs)$/,
                use: [
                    {
                        loader: 'babel-loader',
                        options: {
                            presets: [
                                ['@babel/preset-env', {targets: "defaults"}]
                            ],
                        }
                    }
                ]
            },
            {
                test: /\.s[ac]ss/i,
                use: [
                    'style-loader',
                    {
                        loader: 'css-loader',
                        options: {
                            sourceMap: true
                        }
                    },
                    {
                        loader: 'sass-loader',
                        options: {
                            sourceMap: true
                        }
                    }
                ]
            }
        ]
    }
};