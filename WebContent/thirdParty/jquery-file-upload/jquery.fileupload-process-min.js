!function(e){"use strict";"function"==typeof define&&define.amd?define(["jquery","./jquery.fileupload"],e):e(window.jQuery)}(function(e){"use strict";var s=e.blueimp.fileupload.prototype.options.add;e.widget("blueimp.fileupload",e.blueimp.fileupload,{options:{processQueue:[],add:function(i,r){var o=e(this);r.process(function(){return o.fileupload("process",r)}),s.call(this,i,r)}},processActions:{},_processFile:function(s,i,r){var o=this,n={files:s,index:i,options:r},t=e.Deferred().resolveWith(o,[n]),u=t.promise();return this._trigger("process",null,n),e.each(r.processQueue,function(e,s){var i=function(e){return o.processActions[s.action].call(o,e,s)};u=u.pipe(i,s.always&&i)}),u.done(function(){o._trigger("processdone",null,n),o._trigger("processalways",null,n)}).fail(function(){o._trigger("processfail",null,n),o._trigger("processalways",null,n)}),u},_transformProcessQueue:function(s){var i=[];e.each(s.processQueue,function(){var r={};e.each(this,function(i,o){"string"===e.type(o)&&"@"===o.charAt(0)?r[i]=s[o.slice(1)]:r[i]=o}),i.push(r)}),s.processQueue=i},processing:function(){return this._processing},process:function(s){var i=this,r=e.extend({},this.options,s);return r.processQueue&&r.processQueue.length&&(this._transformProcessQueue(r),0===this._processing&&this._trigger("processstart"),e.each(s.files,function(e,o){i._processing+=1;var n=function(){return i._processFile(s.files,e,r)};i._processingQueue=i._processingQueue.pipe(n,n).always(function(){i._processing-=1,0===i._processing&&i._trigger("processstop")})})),this._processingQueue},_create:function(){this._super(),this._processing=0,this._processingQueue=e.Deferred().resolveWith(this).promise()}})});