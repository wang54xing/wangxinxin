// JavaScript Document
define(['jquery'], function (require, exports, module) {
    jQuery.extend({

        createUploadIframe: function (id, uri) {
            //create frame
            var frameId = 'jUploadFrame' + id;

            //jQuery 从 1.9 版开始，移除了 $.browser 和 $.browser.version ， 取而代之的是 $.support 
            // ie下使用1.9及其以上版本会报错
            // 修改代码
            var ieVersion = navigator.appVersion.split(";")[1].replace(/[ ]/g, "").replace("MSIE","");
            if (window.ActiveXObject) {
                if (ieVersion == "9.0" || ieVersion == "10.0") { //jQuery.browser.version == "9.0"
                    io = document.createElement('iframe');
                    io.id = frameId;
                    io.name = frameId;
                } else if (ieVersion == "6.0" || ieVersion == "7.0" || ieVersion == "8.0") { //jQuery.browser.version == "6.0" || jQuery.browser.version == "7.0" || jQuery.browser.version == "8.0"
                    var io = document.createElement('<iframe id="' + frameId + '" name="' + frameId + '" />');
                    if (typeof uri == 'boolean') {
                        io.src = 'javascript:false';
                    }
                    else if (typeof uri == 'string') {
                        io.src = uri;
                    }
                }
            }
            else {
                var io = document.createElement('iframe');
                io.id = frameId;
                io.name = frameId;
            }
            io.style.position = 'absolute';
            io.style.top = '-1000px';
            io.style.left = '-1000px';

            document.body.appendChild(io);

            return io;
        },
        createUploadForm: function (id, fileElementId, param) {
            //create form
            var formId = 'jUploadForm' + id;
            var fileId = 'jUploadFile' + id;
            var form = jQuery('<form  action="" method="POST" name="' + formId + '" id="' + formId + '" enctype="multipart/form-data"></form>');
            if (param) {
                for (var key in param) {
                    form.append("<input type='hidden' name='" + key + "' value='" + param[key] + "'>");
                }
            }

            var oldElement = jQuery('#' + fileElementId);
            var newElement = jQuery(oldElement).clone();
            jQuery(oldElement).attr('id', fileId);
            jQuery(oldElement).before(newElement);
            jQuery(oldElement).appendTo(form);
            //set attributes
            jQuery(form).css('position', 'absolute');
            jQuery(form).css('top', '-1200px');
            jQuery(form).css('left', '-1200px');
            jQuery(form).appendTo('body');
            return form;
        },

        ajaxFileUpload: function (s) {
            // TODO introduce global settings, allowing the client to modify them for all requests, not only timeout
            s = jQuery.extend({}, jQuery.ajaxSettings, s);
            var id = s.fileElementId;
            var form = jQuery.createUploadForm(id, s.fileElementId, s.param);
            var io = jQuery.createUploadIframe(id, s.secureuri);
            var frameId = 'jUploadFrame' + id;
            var formId = 'jUploadForm' + id;

            if (s.global && !jQuery.active++) {
                // Watch for a new set of requests
                jQuery.event.trigger("ajaxStart");
            }
            var requestDone = false;
            // Create the request object
            var xml = {};
            if (s.global) {
                jQuery.event.trigger("ajaxSend", [xml, s]);
            }

            var uploadCallback = function (isTimeout) {
                // Wait for a response to come back
                var io = document.getElementById(frameId);
                try {
                    if (io.contentWindow) {
                        xml.responseText = io.contentWindow.document.body ? io.contentWindow.document.body.innerHTML : null;
                        xml.responseXML = io.contentWindow.document.XMLDocument ? io.contentWindow.document.XMLDocument : io.contentWindow.document;

                    } else if (io.contentDocument) {
                        xml.responseText = io.contentDocument.document.body ? io.contentDocument.document.body.innerHTML : null;
                        xml.responseXML = io.contentDocument.document.XMLDocument ? io.contentDocument.document.XMLDocument : io.contentDocument.document;
                    }
                } catch (e) {
                    jQuery.handleError(s, xml, null, e);
                }
                if (xml || isTimeout == "timeout") {
                    requestDone = true;
                    var status;
                    try {
                        status = isTimeout != "timeout" ? "success" : "error";
                        // Make sure that the request was successful or notmodified
                        if (status != "error") {
                            // process the data (runs the xml through httpData regardless of callback)
                            var data = jQuery.uploadHttpData(xml, s.dataType);
                            if (s.success) {
                                // ifa local callback was specified, fire it and pass it the data
                                s.success(data, status);
                            }
                            ;
                            if (s.global) {
                                // Fire the global callback
                                jQuery.event.trigger("ajaxSuccess", [xml, s]);
                            }
                            ;
                        } else {
                            s.error(data,status);
                        }

                    } catch (e) {
                        status = "error";
                        s.error(data,status);
                    }
                    ;
                    if (s.global) {
                        // The request was completed
                        jQuery.event.trigger("ajaxComplete", [xml, s]);
                    }
                    ;


                    // Handle the global AJAX counter
                    if (s.global && !--jQuery.active) {
                        jQuery.event.trigger("ajaxStop");
                    }
                    ;
                    if (s.complete) {
                        s.complete(xml, status);
                    }
                    ;

                    jQuery(io).unbind();

                    setTimeout(function () {
                        try {
                            jQuery(io).remove();
                            jQuery(form).remove();

                        } catch (e) {
                        }

                    }, 100);

                    xml = null;

                }
                ;
            }
            // Timeout checker
            if (s.timeout > 0) {
                setTimeout(function () {

                    if (!requestDone) {
                        // Check to see ifthe request is still happening
                        uploadCallback("timeout");
                    }

                }, s.timeout);
            }
            try {
                var form = jQuery('#' + formId);
                jQuery(form).attr('action', s.url);
                jQuery(form).attr('method', 'POST');
                jQuery(form).attr('target', frameId);
                if (form.encoding) {
                    form.encoding = 'multipart/form-data';
                }
                else {
                    form.enctype = 'multipart/form-data';
                }
                jQuery(form).submit();

            } catch (e) {
                jQuery.handleError(s, xml, null, e);
            }
            if (window.attachEvent) {
                document.getElementById(frameId).attachEvent('onload', uploadCallback);
            }
            else {
                document.getElementById(frameId).addEventListener('load', uploadCallback, false);
            }
            return {
                abort: function () {
                }
            };

        },

        uploadHttpData: function (r, type) {
            var data = !type;
            data = type == "xml" || data ? r.responseXML : r.responseText;
            // ifthe type is "script", eval it in global context
            if (type == "script") {
                jQuery.globalEval(data);
            }

            // Get the JavaScript object, ifJSON is used.
            if (type == "json") {
                eval("data = " + data);
            }

            // evaluate scripts within html
            if (type == "html") {
                jQuery("<div>").html(data).evalScripts();
            }

            return data;
        }
    });
    module.exports = jQuery;
});
