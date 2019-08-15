define('ycode',['jquery'],function(){
    (function ($) {

        "use strict";

        var Ycode = function (element, options) {

            this.options = $.extend({}, $.fn.ycode.defaults, options);

            this.num_completed_fx = 0;

            this.is_done = false;

            this.monitor_timer = null;

            this.killswitch=null;

            this.$element = $(element);

            if(this.options.restore)
                this.original_html = this.$element.html();

            this.init();

        }

        Ycode.prototype.init = function(){

            this.new_html = this.$element.text().replace( this.options.pattern, this.options.replacement );

            this.$element.addClass(this.options.css.element.base).addClass( this.options.css.element.before );

            this.$element.html( this.new_html );

            this.$letters = this.$element.find(this.options.selector);

            this.$letters
                .css('transition-duration', this.options.fx_duration)
                .addClass(this.options.css.letters.base)
                .addClass(this.options.css.letters.before);

            this.bindYcodeEnd();

            this.num_letters = this.$letters.length;


            this.fx();

            return this;
        }

        Ycode.prototype.bindYcodeEnd = function(){
            var options = this.options;
            var lfx = this;
            this.$letters.bind("transitionend", function(){
                options.onLetterComplete( $(this), lfx.$element, lfx );
                lfx.notifyFXEnd( );

                switch(options.letter_end){
                    case "destroy":
                        $(this).remove();
                        break;

                    case "rewind":
                        lfx.applyYcode( $(this), options.timing, options.css.letters.after, options.css.letters.before );
                        break;

                    case "stay":
                        break;

                    // restore
                    default:
                        $(this).replaceWith( $(this).text() );
                }

            });
            return lfx;
        }

        Ycode.prototype.terminate=function(){
            this.is_done = true;
            this.options.onElementComplete(this.$element, this);

            clearTimeout(this.killswitch);

            switch(this.options.element_end){
                case "destroy":
                    this.$element.remove();
                    break;

                case "stay":
                    break;

                // restore
                default:
                    this.$element.html( this.original_html );
                    this.$element.removeClass( this.options.css.element.base ).removeClass(this.options.css.element.after);
                    break;
            }

        }

        Ycode.prototype.notifyFXEnd=function(){
            clearTimeout(this.monitor_timer);
            this.num_completed_fx++;

            var lfx = this;
            this.monitor_timer = setTimeout(
                function(){
                    if(lfx.num_completed_fx % lfx.num_letters===0){
                        lfx.terminate();
                    }
                },
                Math.max(this.options.timing+10, 50)
            );

            return this;
        }

        Ycode.prototype.startKillWatch = function(){
            var fx_duration = this.options.fx_duration.match(/\d+s/) ? parseInt(this.options.fx_duration) : 1;
            var time = Math.ceil(1.5 * this.num_letters * this.options.timing * fx_duration );
            var lfx = this;
            this.killswitch = window.setTimeout(function(){
                if(!lfx.isDone()){
                    lfx.terminate()
                }
            }, time)
        }

        Ycode.prototype.fx = function(){
            var lfx = this;
            this.startKillWatch();
            this.$element.removeClass(this.options.css.element.before).addClass(this.options.css.element.after);
            var $letters = this.options.sort(this.$letters);
            var options = this.options;



            $letters.each(
                function(i, letter){
                    lfx.applyYcode( $(letter), (i+1)*options.timing, options.css.letters.before, options.css.letters.after);
                }
            );
            return this;
        }

        Ycode.prototype.applyYcode = function($letter, timing, css_before, css_after){
            var options = this.options;

            window.setTimeout(
                function(){
                    $letter.removeClass(css_before).addClass(css_after);
                },
                timing
            );
            return this
        }

        Ycode.prototype.isDone = function(){
            return this.is_done;
        }




        var YcodeConfig = function(conf){
            this.config = $.extend({}, $.fn.ycode.defaults, conf);
            this.buildCss(this.config.backwards);

            // check & change to word pattern
            if(this.config.words)
                this.config.pattern=/(\S+)/g;


        }


        YcodeConfig.prototype.buildCss = function(flip){
            var options = this.config;
            var before = flip ? 'after' : 'before';
            var after = flip ? 'before' : 'after';


            var css = { element:{}, letters:{} };

            css.element.base=options.element_class+ "-container " + options.fx.replace(/(\S+)/g, options.element_class + "-$1-container");
            css.element[before]=options.fx.replace(/(\S+)/g, options.element_class + "-$1-before-container");
            css.element[after]=options.fx.replace(/(\S+)/g, options.element_class + "-$1-after-container");

            css.letters.base = options.element_class;
            css.letters[before] = options.fx.replace(/(\S+)/g, options.element_class + "-$1-before")
            css.letters[after] = options.fx.replace(/(\S+)/g, options.element_class + "-$1-after")

            this.config = $.extend( options, {'css':css} );
        }

        YcodeConfig.prototype.getConfig = function(){
            return this.config;
        }

        YcodeConfig.parse=function(config){
            return (new YcodeConfig( config )).getConfig();
        }




        $.fn.ycode=function(config){

            config = YcodeConfig.parse( config );

            return $(this).each(function(){
                var $element = $(this);
                if (! $element.data('ycode-obj') || $element.data('ycode-obj').isDone() ){
                    $element.data('ycode-obj', new Ycode( $element, config ) );
                }
            });

        };

        $.fn.ycode.sort={
            random:function(array){
                var currentIndex = array.length, temporaryValue, randomIndex;

                // While there remain elements to shuffle...
                while (0 !== currentIndex) {

                    // Pick a remaining element...
                    randomIndex = Math.floor(Math.random() * currentIndex);
                    currentIndex -= 1;

                    // And swap it with the current element.
                    temporaryValue = array[currentIndex];
                    array[currentIndex] = array[randomIndex];
                    array[randomIndex] = temporaryValue;
                }

                return array;
            },
            reverse:function($array){
                return $array.toArray().reverse();
            }
        }

        $.fn.ycode.patterns={
            letters:/(\S)/gi
        }

    // Plugin Configurables
        $.fn.ycode.defaults = {

            // Default fx
            fx:'spin fly-top',

            // defaults to selecting all characters
            pattern:/(\S)/gi,

            // switch from letter FX to word FX.
            word:false,

            // fx, like fly or fade, can happen in (eg fade-in) or out (eg fade-out).
            // The default is in; change backwards to true to reverse the order of the effects.
            backwards:false,

            // defaults to injecting spans, can be any inline element
            replacement:"<span>$1</span>",

            //selector -- should match replacement above
            selector:'span',

            // letter fx start sequentially: letters start their fx one after another.
            // this sets time between the letters
            timing:50,

            //duration of each fx
            // options the same as css property for transition-duration ("1ms", "1s", etc)
            fx_duration:"1s",


            // stabile dimensions
            // stabilize:true,

            // sort callback for custom sorting elements
            sort:function($letters){ return $letters; },


            // Callback when letter is done animating. Runs after each letter
            onLetterComplete:function($letter, $element, LetterFXObj){},

            // Runs after all are done.
            onElementComplete:function($element, LetterFXObj){},


            // what to do when a letter completes its animation.
            // options include
            //    restore: return letter to plain text (default)
            //    destroy: get rid of the letter.
            //    stay: leave it as is.
            //    rewind: reverse the animation
            letter_end:"restore",


            // what to do when the entire element has completed all its letter effects
            // options include:
            //    restore: return element to its pre-fx state (default)
            //    stay: do nothing
            //    destroy: get rid of the element... FOREVER
            element_end:"restore",

            // Restore container element back to original state.
            // options: true, false, "element" ("element" waits until all letters complete fx before restoring)
            restore:true,

            // destroy element/letters after fx, useful on {out:true} fx
            // options: true, false, "letters" ("letters" destroys each letter after fx, but elements original content may be restored after all letters complete fx)
            destroy:false,

            // default class for injected elements
            element_class:'ycode',

            // placeholder values that are calculated on launch
            css:{
                element:{ base:'', before:'', after:''},
                letters:{ base:'', before:'', after:''}
            }



        };

    }(jQuery));
})
