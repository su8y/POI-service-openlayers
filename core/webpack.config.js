const path = require('path');
module.exports = {
    main: './src/main/resources/static/js/index.js',
    output: {
        path: path.resolve(__dirname, 'src/main/resources/static/js/out'),
        filename: '[name].bundle.js'
    }
};