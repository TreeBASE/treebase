/**
 * Native DOM API Compatibility Layer
 * 
 * This module provides native JavaScript replacements for Prototype.js functionality.
 * It adds helper methods to Element.prototype using modern DOM APIs.
 * 
 * Functions replaced:
 * - $('id') -> document.getElementById('id') or $tb('id')
 * - $$('.class') -> document.querySelectorAll('.class') or $$tb('.class')
 * - element.select('selector') -> element.querySelectorAll('selector')
 * - element.update(content) -> element.innerHTML = content
 * - element.addClassName('class') -> element.classList.add('class')
 * - element.removeClassName('class') -> element.classList.remove('class')
 * - element.hasClassName('class') -> element.classList.contains('class')
 * - element.observe('event', handler) -> element.addEventListener('event', handler)
 * - element.firstDescendant() -> element.firstElementChild
 * - new Element('tag', attrs) -> createTBElement('tag', attrs)
 * - array.invoke('method', args) -> array.forEach(el => el.method(args))
 * - new Ajax.Request(url, opts) -> tbFetch(url, opts)
 */

(function(global) {
    'use strict';

    /**
     * DOM Selection helper - replaces $('id')
     * @param {string} id - Element ID to find
     * @returns {Element|null} The found element or null
     */
    global.$tb = function(id) {
        if (typeof id === 'string') {
            return document.getElementById(id);
        }
        return id; // Already an element
    };

    /**
     * DOM Selection helper for CSS selectors - replaces $$('selector')
     * @param {string} selector - CSS selector
     * @param {Element} [context=document] - Context element to search within
     * @returns {NodeList} NodeList of matching elements
     */
    global.$$tb = function(selector, context) {
        context = context || document;
        return context.querySelectorAll(selector);
    };

    /**
     * Element creation helper - replaces new Element('tag', attrs)
     * @param {string} tagName - HTML tag name
     * @param {Object} [attrs] - Attributes to set on the element
     * @returns {Element} The created element
     */
    global.createTBElement = function(tagName, attrs) {
        var element = document.createElement(tagName);
        if (attrs) {
            for (var key in attrs) {
                if (attrs.hasOwnProperty(key)) {
                    if (key === 'className' || key === 'class') {
                        element.className = attrs[key];
                    } else if (key === 'htmlFor') {
                        element.htmlFor = attrs[key];
                    } else if (key === 'style' && typeof attrs[key] === 'object') {
                        for (var styleProp in attrs[key]) {
                            if (attrs[key].hasOwnProperty(styleProp)) {
                                element.style[styleProp] = attrs[key][styleProp];
                            }
                        }
                    } else {
                        element.setAttribute(key, attrs[key]);
                    }
                }
            }
        }
        return element;
    };

    /**
     * AJAX helper - replaces new Ajax.Request()
     * Uses the modern fetch API with callback-style interface for compatibility
     * @param {string} url - URL to fetch
     * @param {Object} options - Request options
     * @param {string} [options.method='GET'] - HTTP method
     * @param {Function} [options.onSuccess] - Success callback (receives response object)
     * @param {Function} [options.onFailure] - Failure callback (receives error)
     * @param {Object} [options.parameters] - Request parameters (for POST)
     * @param {Object} [options.requestHeaders] - Additional request headers
     */
    global.tbFetch = function(url, options) {
        options = options || {};
        var method = (options.method || 'GET').toUpperCase();
        
        var fetchOptions = {
            method: method,
            headers: options.requestHeaders || {}
        };

        // Handle parameters for POST requests
        if (options.parameters && method === 'POST') {
            if (typeof options.parameters === 'string') {
                fetchOptions.body = options.parameters;
                fetchOptions.headers['Content-Type'] = 'application/x-www-form-urlencoded';
            } else {
                var formData = new FormData();
                for (var key in options.parameters) {
                    if (options.parameters.hasOwnProperty(key)) {
                        formData.append(key, options.parameters[key]);
                    }
                }
                fetchOptions.body = formData;
            }
        }

        fetch(url, fetchOptions)
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text().then(function(text) {
                    // Create a Prototype.js-compatible response object
                    return {
                        responseText: text,
                        status: response.status,
                        statusText: response.statusText,
                        getHeader: function(name) {
                            return response.headers.get(name);
                        }
                    };
                });
            })
            .then(function(responseObj) {
                if (options.onSuccess && typeof options.onSuccess === 'function') {
                    options.onSuccess(responseObj);
                }
            })
            .catch(function(error) {
                if (options.onFailure && typeof options.onFailure === 'function') {
                    options.onFailure(error);
                } else {
                    console.error('tbFetch error:', error);
                }
            });
    };

    /**
     * Add Prototype.js-compatible methods to Element.prototype if they don't exist
     * This allows existing code to continue working with minimal changes
     */
    
    // select(selector) - finds descendants matching selector
    if (!Element.prototype.select) {
        Element.prototype.select = function(selector) {
            // Handle multiple selectors (like prototype's select('input','textarea'))
            if (arguments.length > 1) {
                selector = Array.prototype.slice.call(arguments).join(', ');
            }
            return Array.prototype.slice.call(this.querySelectorAll(selector));
        };
    }

    // update(content) - sets innerHTML
    if (!Element.prototype.update) {
        Element.prototype.update = function(content) {
            if (content === null || content === undefined) {
                content = '';
            }
            this.innerHTML = content;
            return this;
        };
    }

    // addClassName(className) - adds a CSS class
    if (!Element.prototype.addClassName) {
        Element.prototype.addClassName = function(className) {
            this.classList.add(className);
            return this;
        };
    }

    // removeClassName(className) - removes a CSS class
    if (!Element.prototype.removeClassName) {
        Element.prototype.removeClassName = function(className) {
            this.classList.remove(className);
            return this;
        };
    }

    // hasClassName(className) - checks if element has a CSS class
    if (!Element.prototype.hasClassName) {
        Element.prototype.hasClassName = function(className) {
            return this.classList.contains(className);
        };
    }

    // toggleClassName(className) - toggles a CSS class
    if (!Element.prototype.toggleClassName) {
        Element.prototype.toggleClassName = function(className) {
            this.classList.toggle(className);
            return this;
        };
    }

    // observe(eventName, handler) - adds event listener
    if (!Element.prototype.observe) {
        Element.prototype.observe = function(eventName, handler) {
            this.addEventListener(eventName, handler);
            return this;
        };
    }

    // stopObserving(eventName, handler) - removes event listener
    if (!Element.prototype.stopObserving) {
        Element.prototype.stopObserving = function(eventName, handler) {
            this.removeEventListener(eventName, handler);
            return this;
        };
    }

    // firstDescendant() - gets first element child
    if (!Element.prototype.firstDescendant) {
        Element.prototype.firstDescendant = function() {
            return this.firstElementChild;
        };
    }

    // setStyle(styles) - sets multiple CSS styles
    if (!Element.prototype.setStyle) {
        Element.prototype.setStyle = function(styles) {
            for (var property in styles) {
                if (styles.hasOwnProperty(property)) {
                    this.style[property] = styles[property];
                }
            }
            return this;
        };
    }

    // getStyle(property) - gets a CSS style value
    if (!Element.prototype.getStyle) {
        Element.prototype.getStyle = function(property) {
            return window.getComputedStyle(this).getPropertyValue(property);
        };
    }

    // disable() - disables a form element
    if (!Element.prototype.disable) {
        Element.prototype.disable = function() {
            this.disabled = true;
            return this;
        };
    }

    // enable() - enables a form element
    if (!Element.prototype.enable) {
        Element.prototype.enable = function() {
            this.disabled = false;
            return this;
        };
    }

    /**
     * Add invoke method to NodeList and Array for batch operations
     * This allows patterns like $$('.items').invoke('setStyle', { display: 'block' })
     */
    var addInvokeMethod = function(proto) {
        if (!proto.invoke) {
            proto.invoke = function(methodName) {
                var args = Array.prototype.slice.call(arguments, 1);
                var results = [];
                for (var i = 0; i < this.length; i++) {
                    var element = this[i];
                    if (element && typeof element[methodName] === 'function') {
                        results.push(element[methodName].apply(element, args));
                    }
                }
                return results;
            };
        }
    };

    addInvokeMethod(NodeList.prototype);
    addInvokeMethod(Array.prototype);

    /**
     * Add size() method to NodeList and Array for Prototype.js compatibility
     */
    var addSizeMethod = function(proto) {
        if (!proto.size) {
            proto.size = function() {
                return this.length;
            };
        }
    };

    addSizeMethod(NodeList.prototype);
    addSizeMethod(Array.prototype);

})(typeof window !== 'undefined' ? window : this);
