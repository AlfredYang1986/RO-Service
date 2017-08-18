/**
 * Created by clock on 2017/7/31.
 */
function budgetTemplate(){
    try {
        location.href = "/common/downloadFile/产品预算数据模板.xlsx";
    } catch (err){
        PetAlert(getMessageByName(err.name));
    }
}

function optimizationTemplate(){
    try {
        location.href = "/common/downloadFile/产品优化数据模板.xlsx";
    } catch (err){
        PetAlert(getMessageByName(err.name));
    }
}
var target_collection = "";
function budgetImport(){
    target_collection = "budget";
}

function optimizationImport(){
    target_collection = "productOptimization";
}

function startImport(collection_name,file_path){
    try {
        var data = JSON.stringify({
            "token" : $.session.get("token"),
            "collection_name" : collection_name,
            "file_path" : file_path
        });
        AjaxData("/data/import", data, "POST", function (data) {
            if(data.status === "ok"){
                $.tooltip("数据导入成功！", 2000, true, function () {
                    $.closeDialog(function () {
                        location.reload();
                    });
                });
            }else{
                if(collection_name === "budget"){
                    throw new PetException('product budget data import failed');
                } else {
                    throw new PetException('product optimization data import failed');
                }
            }
        }, function (e) {
            throw new PetException(e.name);
        })
    } catch (err) {
        PetAlert(getMessageByName(err.name));
    }
}

/*
 * Author: clock
 * Based on liwei's Client
 */
jQuery(function() {
    var uploader = new Array();                         //  参数说明: 创建uploader实例数组
    var fileCount = new Array();                        //  参数说明: 创建实例文件数量数组
    var fileSize = new Array();                         //  参数说明: 创建实例文件大小数组
    var percentages = new Array();                      //  参数说明: 实例内所有文件进度信息数组
    var state = new Array();                            //  参数说明: uploder状态数组

    //  可行性判断
    if ( !WebUploader.Uploader.support() ) {
        alert( 'Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
        throw new Error( 'WebUploader does not support the browser you are using.' );
    }

    //循环页面中每个上传域
    $('.uploder-container').each(function(index){
        var now_uploader=now_uploader;

        var ratio = window.devicePixelRatio || 1,                           //  参数说明: 物理像素/独立像素 默认设置为1
            thumbnailWidth = 100 * ratio,                                       //  参数说明: 缩略图大小(宽)
            thumbnailHeight = 100 * ratio,                                      //  参数说明: 缩略图大小(高)

            supportTransition = (function(){
                var s = document.createElement('p').style,
                    r = 'transition' in s || 'WebkitTransition' in s || 'MozTransition' in s || 'msTransition' in s || 'OTransition' in s;
                s = null;
                return r;
            })();

        var filePicker=$(this).find('.filePicker');                         //  : 上传按钮实例
        var queueList=$(this).find('.queueList');                           //  : 拖拽容器实例
        var jxfilePicker=$(this).find('.jxfilePicker');                     //  : 继续添加按钮实例
        var placeholder=$(this).find('.placeholder');                       //  : 按钮与虚线框实例
        var statusBar=$(this).find('.statusBar');                           //  : 再次添加按钮容器实例
        var info =statusBar.find('.info');                                  //  : 提示信息容器实例
        var upload = $(this).find('.importBtn');                            //  : 上传按钮容器实例

        var queue = $('<ul class="filelist"></ul>').appendTo( queueList);   //  : 文件容器实例
        percentages[index] = {};                                            //  参数说明: 所有文件的进度信息，key为file id
        state[index] = 'pedding';                                           //  参数说明: 状态
        var progress = statusBar.find( '.progress' ).hide();                //  参数说明: 进度条实例

        fileCount[index] = 0;                                               //  : 添加的文件数量
        fileSize[index] = 0;                                                //  : 添加的文件总大小

        //  : 初始化上传实例
        now_uploader = WebUploader.create({
            pick: {                                 //  参数说明: {Selector, Object} [可选] [默认值：undefined] 指定选择文件的按钮容器，不指定则不创建按钮.
                id: filePicker,                     //  参数说明: {Seletor|dom} 指定选择文件的按钮容器，不指定则不创建按钮。注意 这里虽然写的是 id, 但是不是只支持 id, 还支持 class, 或者 dom 节点.
                innerHTML: '点击选择文件'             //  参数说明: {String}
            },
            dnd: queueList,                         //  参数说明: {Selector} [可选] [默认值：undefined] 指定Drag And Drop拖拽的容器，如果不指定，则不启动.
            accept: {                               //  参数说明: {Arroy} [可选] [默认值：null] 指定接受哪些类型的文件.由于目前还有ext转mimeType表，所以这里需要分开指定.
                title: 'intoTypes',                 //  参数说明: {String} 文字描述.
                extensions: 'csv,xlsx,xls',         //  参数说明: {String} 允许的文件后缀，不带点，多个用逗号分割.
                mimeTypes: '.csv,.xlsx,.xls'        //  参数说明: {String} 多个用逗号分割.
            },
            disableGlobalDnd: true,                 //  参数说明: {Selector} [可选] [默认值：false] 是否禁掉整个页面的拖拽功能，如果不禁用，图片拖进来的时候会默认被浏览器打开.
            chunked: true,                          //  参数说明: {Boolean} [可选] [默认值：false] 是否要分片处理大文件上传.
            chunkSize: 5242880,                     //  参数说明: {Boolean} [可选] [默认值：5242880] 如果要分片，分多大一片？ 默认大小为5M.
            chunkRetry: 3,                          //  参数说明: {Boolean} [可选] [默认值：2] 如果某个分片由于网络问题出错，允许自动重传多少次？
            threads: 1,                             //  参数说明: {Boolean} [可选] [默认值：3] 上传并发数。允许同时最大上传进程数.
            server: '/common/uploadFile',           //  参数说明: {String} [必选] 文件接收服务端.
            fileVal: 'file',                        //  参数说明: {Object} [可选] [默认值：'file'] 设置文件上传域的name.
            method: 'POST',                         //  参数说明: {Object} [可选] [默认值：'POST'] 文件上传方式，POST或者GET.
            fileNumLimit: 1,                        //  参数说明: {int} [可选] [默认值：undefined] 验证文件总数量, 超出则不允许加入队列.
            fileSizeLimit: 5242880000,              //  参数说明: {int} [可选] [默认值：undefined] 验证文件总大小是否超出限制, 超出则不允许加入队列.
            fileSingleSizeLimit: 524288000,         //  参数说明: {int} [可选] [默认值：undefined] 验证单个文件大小是否超出限制, 超出则不允许加入队列.
            auto : false,                           //  参数说明: {Boolean} [可选] [默认值：false] 设置为 true 后，不需要手动调用上传，有文件选择即开始上传.
            formData: {                             //  参数说明: {Object} [可选] [默认值：{}] 文件上传请求的参数表，每次发送都会发送此对象中的参数.
                token: index                      // : 唯一标示
            },
            compress: false                         //  参数说明: {Object} [可选] 配置压缩的图片的选项。如果此选项为false, 则图片在上传前不进行压缩
        });

        //  : 添加“添加文件”的按钮
        now_uploader.addButton({
            id: jxfilePicker,
            innerHTML: '添加文件'
        });


        // XXX : 当一批文件添加进队列以后触发。
        now_uploader.on('fileQueued', function( file ) {
            fileCount[index] = fileCount[index]+1;
            fileSize[index] += file.size;
            //console.info(now_uploader)
            //console.info(index)
            if ( fileCount[index] === 1 ) {
                placeholder.addClass( 'element-invisible' );
                statusBar.show();
            }
            addFile(file);
            setState('ready');
            updateTotalProgress();
        });
        // XXX : 当文件被移除队列后触发。
        now_uploader.on('fileDequeued', function( file ) {
            fileCount[index] = fileCount[index]-1;
            fileSize[index] -= file.size;
            //console.info(fileCount[index])
            //console.info(index)
            if ( !fileCount[index] ) {
                setState('pedding');
            }
            removeFile( file );
            updateTotalProgress();
        });
        // XXX : 上传过程中触发，携带上传进度
        now_uploader.on('uploadProgress', function( file, percentage ) {
            //uploadProgress
            var $li = $('#'+file.id), $percent = $li.find('.progress span');
            $percent.css( 'width', percentage * 100 + '%' );
            percentages[index][ file.id ][ 1 ] = percentage;
            updateTotalProgress();
        });


        // XXX : 当文件上传成功时触发。
        now_uploader.on('uploadSuccess',function(file,reponse){
            if(reponse.status === "ok"){
                if(reponse.file_path !== "") {
                    if (target_collection !== "") {
                        startImport(target_collection, reponse.file_path)
                    }
                }
            }
        });

        // XXX : 捕捉uploader事件类型，并赋值状态
        now_uploader.on( 'all', function( type ) {
            switch( type ) {
                case 'uploadFinished':
                    setState( 'confirm');
                    break;

                case 'startUpload':
                    setState( 'uploading');
                    break;

                case 'stopUpload':
                    setState( 'paused');
                    break;

            }
        });

        // XXX : 当validate不通过时，会以派送错误事件的形式通知调用者。通过upload.on('error', handler)可以捕获到此类错误，目前有以下错误会在特定的情况下派送错来
        now_uploader.on('error', function( handler ) {
            switch(handler) {
                case 'Q_EXCEED_NUM_LIMIT':
                    alert('添加的文件数量超出文件数量限制。');
                    break;
                case 'Q_EXCEED_SIZE_LIMIT':
                    alert('添加的文件总大小超出文件大小限制。');
                    break;
                case 'Q_TYPE_DENIED':
                    alert('文件类型有误。');
                    break;
            }
        });

        //*********************************************************************
        //功能: 上传
        //时间：20170410
        //说明：点击上传按钮后触发。
        //*********************************************************************
        upload.on('click', function() {
            now_uploader.options.formData.user_name = $.cookie("user_name");
            if ($(this).hasClass('disabled')) {
                return false;
            }
            if ( state[index] === 'ready' ) {
                now_uploader.upload();
            } else if ( state[index] === 'paused' ) {
                now_uploader.upload();
            } else if ( state[index] === 'uploading' ) {
                now_uploader.stop();
            }
        });

        //*********************************************************************
        //功能: 文件元素框实例创建
        //时间：20170410
        //说明：当有文件添加进来时执行，负责view的创建。
        //*********************************************************************
        function addFile(file) {
            var $li = $( '<li id="' + file.id + '">' +
                    '<p class="title">' + file.name + '</p>' +
                    '<p class="imgWrap"></p>'+
                    '<p class="progress"><span></span></p>' +
                    '</li>' ),

                $btns = $('<div class="file-panel">' +
                    '<span class="cancel">删除</span>' +
                    '<span class="rotateRight">向右旋转</span>' +
                    '<span class="rotateLeft">向左旋转</span></div>').appendTo( $li ),
                $prgress = $li.find('p.progress span'),
                $wrap = $li.find( 'p.imgWrap' ),
                $info = $('<p class="error"></p>');

            showError = function( code ) {
                switch( code ) {
                    case 'exceed_size':
                        text = '文件大小超出';
                        break;

                    case 'interrupt':
                        text = '上传暂停';
                        break;

                    default:
                        text = '上传失败，请重试';
                        break;
                }

                $info.text( text ).appendTo( $li );
            };

            if ( file.getStatus() === 'invalid' ) {
                showError(file.statusText);
            } else {
                now_uploader.makeThumb( file, function( error, src ) {
                    $wrap.text(WebUploader.formatSize(file.size));
                    return;
                }, thumbnailWidth, thumbnailHeight );
                percentages[index][ file.id ] = [ file.size, 0 ];
                file.rotation = 0;
            }

            file.on('statuschange', function( cur, prev ) {
                if ( prev === 'progress' ) {
                    $prgress.hide().width(0);
                } else if ( prev === 'queued' ) {
                    $li.off( 'mouseenter mouseleave' );
                    $btns.remove();
                }

                // 成功
                if ( cur === 'error' || cur === 'invalid' ) {
                    //console.log( file.statusText );
                    showError(file.statusText);
                    percentages[index][ file.id ][ 1 ] = 1;
                } else if ( cur === 'interrupt' ) {
                    showError( 'interrupt' );
                } else if ( cur === 'queued' ) {
                    percentages[index][ file.id ][ 1 ] = 0;
                } else if ( cur === 'progress' ) {
                    $info.remove();
                    $prgress.css('display', 'block');
                } else if ( cur === 'complete' ) {
                    $li.append( '<span class="success"></span>' );
                }

                $li.removeClass( 'state-' + prev ).addClass( 'state-' + cur );
            });

            $li.on( 'mouseenter', function() {
                $btns.stop().animate({height: 30});
            });

            $li.on( 'mouseleave', function() {
                $btns.stop().animate({height: 0});
            });

            $btns.on( 'click', 'span', function() {
                var index = $(this).index(), deg;
                switch ( index ) {
                    case 0:
                        now_uploader.removeFile( file );
                        return;
                    case 1:
                        file.rotation += 90;
                        break;
                    case 2:
                        file.rotation -= 90;
                        break;
                }

                if ( supportTransition ) {
                    deg = 'rotate(' + file.rotation + 'deg)';
                    $wrap.css({
                        '-webkit-transform': deg,
                        '-mos-transform': deg,
                        '-o-transform': deg,
                        'transform': deg
                    });
                } else {
                    $wrap.css( 'filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ (~~((file.rotation/90)%4 + 4)%4) +')');
                }
            });
            $li.appendTo(queue);
        }

        //*********************************************************************
        //功能: 移除文件元素框实例
        //时间：20170410
        //说明：负责view的销毁。
        //*********************************************************************
        function removeFile( file ) {
            var $li = $('#'+file.id);
            delete percentages[index][ file.id ];
            updateTotalProgress();
            $li.off().find('.file-panel').off().end().remove();
        }

        //*********************************************************************
        //功能: 赋值上传状态
        //时间：20170410
        //说明：负责赋值上传状态。
        //*********************************************************************
        function setState(val) {
            var stats;
            if ( val === state[index] ) {
                return;
            }
            upload.removeClass( 'state-' + state[index] );
            upload.addClass( 'state-' + val );
            state[index] = val;
            switch (state[index]) {
                case 'pedding':
                    placeholder.removeClass( 'element-invisible' );
                    queue.hide();
                    statusBar.addClass( 'element-invisible' );
                    now_uploader.refresh();
                    break;
                case 'ready':
                    placeholder.addClass( 'element-invisible' );
                    jxfilePicker.removeClass( 'element-invisible');
                    queue.show();
                    statusBar.removeClass('element-invisible');
                    now_uploader.refresh();
                    break;
                case 'uploading':
                    jxfilePicker.addClass( 'element-invisible' );
                    progress.show();
                    upload.text( '暂停上传' );
                    break;
                case 'paused':
                    progress.show();
                    upload.text( '继续上传' );
                    break;
                case 'confirm':
                    progress.hide();
                    jxfilePicker.removeClass( 'element-invisible' );
                    upload.text( '开始上传' );
                    stats = now_uploader.getStats();
                    if ( stats.successNum && !stats.uploadFailNum ) {
                        setState( 'finish' );
                        return;
                    }
                    break;
                case 'finish':
                    stats = now_uploader.getStats();
                    if ( stats.successNum ) {
                        $.tooltip('OK, 上传成功！', 2500, true);
                    } else {
                        state[index] = 'done';
                        location.reload();
                    }
                    break;
            }
            updateStatus(state[index]);
        }

        //*********************************************************************
        //功能: 更新总进度
        //时间：20170410
        //说明：负责更新总上传进度。
        //*********************************************************************
        function updateTotalProgress() {
            var loaded = 0,total = 0,spans = progress.children(),percent;
            $.each( percentages[index], function( k, v ) {
                total += v[ 0 ];
                loaded += v[ 0 ] * v[ 1 ];
            } );
            percent = total ? loaded / total : 0;
            spans.eq( 0 ).text( Math.round( percent * 100 ) + '%' );
            spans.eq( 1 ).css( 'width', Math.round( percent * 100 ) + '%' );
            updateStatus();
        }

        //*********************************************************************
        //功能: 更新上传状态
        //时间：20170410
        //说明：负责更新上传状态。
        //*********************************************************************
        function updateStatus() {
            var text = '', stats;
            if ( state[index] === 'ready' ) {
                text = '选中' + fileCount[index] + '个文件，共' + WebUploader.formatSize( fileSize[index] ) + '。';
            } else if ( state[index] === 'confirm' ) {
                stats = now_uploader.getStats();
                if ( stats.uploadFailNum ) {
                    text = '已成功上传' + stats.successNum+ '个文件，'+ stats.uploadFailNum + '个文件上传失败，<a class="retry" href="#">重新上传</a>失败文件或<a class="ignore" href="#">忽略</a>'
                }
            } else {
                stats = now_uploader.getStats();
                text = '共' + fileCount[index] + '个（' + WebUploader.formatSize( fileSize[index] )  + '），已上传' + stats.successNum + '个';
                if (stats.uploadFailNum) {
                    text += '，失败' + stats.uploadFailNum + '个';
                }
            }
            info.html( text );
        }

        //*********************************************************************
        //功能: 重试
        //时间：20170410
        //说明：重试上传失败的文件。
        //*********************************************************************
        info.on( 'click', '.retry', function() {
            uploader.retry();
        } );

        upload.addClass( 'state-' + state[index] );
        updateTotalProgress();
    });

});