;(function(window) {

  var svgSprite = '<svg>' +
    '' +
    '<symbol id="icon-del" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M141.671424 203.503616c0-25.457664 20.614144-46.057472 46.056448-46.057472l230.284288 0L418.01216 111.389696c0-25.457664 20.61312-46.056448 46.056448-46.056448l92.112896 0c25.443328 0 46.057472 20.598784 46.057472 46.056448l0 46.056448 230.284288 0c25.442304 0 46.057472 20.599808 46.057472 46.057472l0 46.056448L141.671424 249.560064 141.671424 203.503616zM832.52224 341.67296l0 92.11392 0 475.920384c0 25.42592-20.614144 46.056448-46.056448 46.056448L233.78432 955.763712c-25.442304 0-46.056448-20.629504-46.056448-46.056448L187.727872 433.78688l0-92.11392 0-46.056448 644.795392 0L832.523264 341.67296zM371.954688 433.78688c0-25.45664-20.614144-46.056448-46.056448-46.056448-25.442304 0-46.056448 20.599808-46.056448 46.056448l0 383.80544c0 25.427968 20.614144 46.058496 46.056448 46.058496 25.442304 0 46.056448-20.629504 46.056448-46.058496L371.954688 433.78688zM556.181504 433.78688c0-25.45664-20.614144-46.056448-46.057472-46.056448-25.44128 0-46.056448 20.599808-46.056448 46.056448l0 383.80544c0 25.427968 20.615168 46.058496 46.056448 46.058496 25.443328 0 46.057472-20.629504 46.057472-46.058496L556.181504 433.78688zM740.409344 433.78688c0-25.45664-20.616192-46.056448-46.058496-46.056448-25.44128 0-46.055424 20.599808-46.055424 46.056448l0 383.80544c0 25.427968 20.614144 46.058496 46.055424 46.058496 25.442304 0 46.058496-20.629504 46.058496-46.058496L740.409344 433.78688z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-38xinzeng" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M1024 625.777778 625.777778 625.777778 625.777778 1024 398.222222 1024 398.222222 625.777778 0 625.777778 0 398.222222 398.222222 398.222222 398.222222 0 625.777778 1.820444 625.777778 398.222222 1024 398.222222Z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-edit" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M326.000622 879.146151l-181.137807-181.137807 500.696096-500.696096 181.137807 181.137807-500.696096 500.696096Z"  ></path>' +
    '' +
    '<path d="M943.446 176.786l-96.23-96.228c-23.446-23.448-63.116-21.792-88.608 3.7l-90.404 90.406 181.138 181.136 90.404-90.404C965.232 239.904 966.89 200.232 943.446 176.786z"  ></path>' +
    '' +
    '<path d="M128.042 726.504 64 960 297.474 895.958Z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '</svg>'
  var script = function() {
    var scripts = document.getElementsByTagName('script')
    return scripts[scripts.length - 1]
  }()
  var shouldInjectCss = script.getAttribute("data-injectcss")

  /**
   * document ready
   */
  var ready = function(fn) {
    if (document.addEventListener) {
      if (~["complete", "loaded", "interactive"].indexOf(document.readyState)) {
        setTimeout(fn, 0)
      } else {
        var loadFn = function() {
          document.removeEventListener("DOMContentLoaded", loadFn, false)
          fn()
        }
        document.addEventListener("DOMContentLoaded", loadFn, false)
      }
    } else if (document.attachEvent) {
      IEContentLoaded(window, fn)
    }

    function IEContentLoaded(w, fn) {
      var d = w.document,
        done = false,
        // only fire once
        init = function() {
          if (!done) {
            done = true
            fn()
          }
        }
        // polling for no errors
      var polling = function() {
        try {
          // throws errors until after ondocumentready
          d.documentElement.doScroll('left')
        } catch (e) {
          setTimeout(polling, 50)
          return
        }
        // no errors, fire

        init()
      };

      polling()
        // trying to always fire before onload
      d.onreadystatechange = function() {
        if (d.readyState == 'complete') {
          d.onreadystatechange = null
          init()
        }
      }
    }
  }

  /**
   * Insert el before target
   *
   * @param {Element} el
   * @param {Element} target
   */

  var before = function(el, target) {
    target.parentNode.insertBefore(el, target)
  }

  /**
   * Prepend el to target
   *
   * @param {Element} el
   * @param {Element} target
   */

  var prepend = function(el, target) {
    if (target.firstChild) {
      before(el, target.firstChild)
    } else {
      target.appendChild(el)
    }
  }

  function appendSvg() {
    var div, svg

    div = document.createElement('div')
    div.innerHTML = svgSprite
    svgSprite = null
    svg = div.getElementsByTagName('svg')[0]
    if (svg) {
      svg.setAttribute('aria-hidden', 'true')
      svg.style.position = 'absolute'
      svg.style.width = 0
      svg.style.height = 0
      svg.style.overflow = 'hidden'
      prepend(svg, document.body)
    }
  }

  if (shouldInjectCss && !window.__iconfont__svg__cssinject__) {
    window.__iconfont__svg__cssinject__ = true
    try {
      document.write("<style>.svgfont {display: inline-block;width: 1em;height: 1em;fill: currentColor;vertical-align: -0.1em;font-size:16px;}</style>");
    } catch (e) {
      console && console.log(e)
    }
  }

  ready(appendSvg)


})(window)