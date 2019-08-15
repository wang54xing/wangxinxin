define(function(require, exports, module) {
	
	var ieSolution = {};
	
	ieSolution.isIE = function() {
    	return navigator.appName == "Microsoft Internet Explorer";
    }
    
	ieSolution.IEVersion = function() {
    	return parseInt(navigator.appVersion.split(";")[1].replace(/[ ]/g, "").replace("MSIE",""));
    }
    
	ieSolution.Map = function() {
    	
    	this.keys = new Array();
    	this.data = new Object();
    	
    	this.set = function(key, value) {
    		if (this.data[key] == null) {
    			if (this.keys.indexOf(key) == -1) {
    				this.keys.push(key);
    			}
    		}
    		this.data[key] = value;
    	}
    	
    	this.get = function(key) {
    		return this.data[key];
    	}
    	
    	this.has = function(key) {
    		return this.keys.indexOf(key) !== -1;
    	}
    }
	
	module.exports = ieSolution;
	
})