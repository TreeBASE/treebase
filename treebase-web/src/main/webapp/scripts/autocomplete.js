/**
 * Self-contained Autocompleter for DWR (Direct Web Remoting)
 * This file provides a standalone autocomplete widget that works with DWR
 * without requiring external dependencies like Prototype.js or Script.aculo.us.
 */

// Create Autocompleter namespace if it doesn't exist
var Autocompleter = Autocompleter || {};

/**
 * Autocompleter.DWR - A DWR-compatible autocomplete widget
 * @param {string} elementId - The ID of the input element
 * @param {string} updateId - The ID of the dropdown container element
 * @param {function} populator - Function that populates choices (receives autocompleter instance and search token)
 * @param {object} options - Configuration options
 */
Autocompleter.DWR = function(elementId, updateId, populator, options) {
    this.element = document.getElementById(elementId);
    this.update = document.getElementById(updateId);
    this.populator = populator;
    this.hasFocus = false;
    this.changed = false;
    this.active = false;
    this.index = 0;
    this.entryCount = 0;
    this.options = {};

    // Set default options and merge with provided options
    this.setOptions(options || {});

    // Initialize event listeners
    this.initializeEventListeners();

    // Hide the update element initially
    this.hide();
};

Autocompleter.DWR.prototype = {
    setOptions: function(options) {
        var self = this;
        var defaults = {
            choices: 10,
            partialSearch: true,
            partialChars: 2,
            ignoreCase: true,
            fullSearch: false,
            minChars: 1,
            frequency: 0.4,
            array: [],
            selector: function(instance) { return self.defaultSelector(instance); },
            valueSelector: function(object) { return object; },
            afterUpdateElement: null
        };

        // Merge defaults with provided options
        for (var key in defaults) {
            if (defaults.hasOwnProperty(key)) {
                this.options[key] = (options && options.hasOwnProperty(key)) ? options[key] : defaults[key];
            }
        }

        // Store the afterUpdateElement callback if provided
        if (this.options.afterUpdateElement) {
            this.afterUpdateCallback = this.options.afterUpdateElement;
        }
    },

    defaultSelector: function(instance) {
        var ret = [];     // Beginning matches
        var partial = []; // Inside matches
        var entry = instance.getToken();
        var valueSelector = instance.options.valueSelector;

        for (var i = 0; i < instance.options.array.length && ret.length < instance.options.choices; i++) {
            var elem = valueSelector(instance.options.array[i]);
            if (typeof elem !== 'string') {
                elem = String(elem);
            }

            var foundPos = instance.options.ignoreCase ?
                elem.toLowerCase().indexOf(entry.toLowerCase()) :
                elem.indexOf(entry);

            while (foundPos !== -1) {
                if (foundPos === 0 && elem.length !== entry.length) {
                    ret.push("<li><strong>" + instance.escapeHtml(elem.substr(0, entry.length)) + "</strong>" +
                        instance.escapeHtml(elem.substr(entry.length)) + "</li>");
                    break;
                } else if (entry.length >= instance.options.partialChars &&
                    instance.options.partialSearch && foundPos !== -1) {
                    if (instance.options.fullSearch || /\s/.test(elem.substr(foundPos - 1, 1))) {
                        partial.push("<li>" + instance.escapeHtml(elem.substr(0, foundPos)) + "<strong>" +
                            instance.escapeHtml(elem.substr(foundPos, entry.length)) + "</strong>" +
                            instance.escapeHtml(elem.substr(foundPos + entry.length)) + "</li>");
                        break;
                    }
                }

                foundPos = instance.options.ignoreCase ?
                    elem.toLowerCase().indexOf(entry.toLowerCase(), foundPos + 1) :
                    elem.indexOf(entry, foundPos + 1);
            }
        }

        if (partial.length) {
            ret = ret.concat(partial.slice(0, instance.options.choices - ret.length));
        }

        return "<ul>" + ret.join('') + "</ul>";
    },

    escapeHtml: function(str) {
        var div = document.createElement('div');
        div.appendChild(document.createTextNode(str));
        return div.innerHTML;
    },

    initializeEventListeners: function() {
        var self = this;

        // Input events
        this.element.addEventListener('keydown', function(e) {
            self.onKeyDown(e);
        });

        this.element.addEventListener('keyup', function(e) {
            self.onKeyUp(e);
        });

        this.element.addEventListener('blur', function(e) {
            // Delay to allow click on dropdown item
            // Store the timeout so it can be cleared if focus returns
            self.blurTimeout = setTimeout(function() {
                self.onBlur(e);
            }, 200);
        });

        this.element.addEventListener('focus', function(e) {
            // Clear any pending blur timeout to avoid race conditions
            if (self.blurTimeout) {
                clearTimeout(self.blurTimeout);
                self.blurTimeout = null;
            }
            self.hasFocus = true;
        });

        // Click events on update element
        this.update.addEventListener('click', function(e) {
            self.onClick(e);
        });
    },

    getToken: function() {
        return this.element.value.trim();
    },

    getUpdatedChoices: function() {
        this.populator(this, this.getToken());
    },

    setChoices: function(array) {
        this.options.array = array;
        this.updateChoices(this.options.selector(this));
    },

    updateChoices: function(html) {
        if (!this.changed && this.hasFocus) {
            this.update.innerHTML = html;
            this.fixPosition();

            // Get list items
            var entries = this.update.querySelectorAll('li');
            this.entryCount = entries.length;

            if (this.entryCount > 0) {
                this.index = 0;
                this.render();
                this.show();
            } else {
                this.hide();
            }
        }
    },

    fixPosition: function() {
        // Position the dropdown below the input
        var rect = this.element.getBoundingClientRect();
        // Use pageXOffset/pageYOffset for IE compatibility, fallback to scrollX/scrollY
        var scrollX = (window.pageXOffset !== undefined) ? window.pageXOffset : window.scrollX;
        var scrollY = (window.pageYOffset !== undefined) ? window.pageYOffset : window.scrollY;
        this.update.style.position = 'absolute';
        this.update.style.left = rect.left + scrollX + 'px';
        this.update.style.top = (rect.bottom + scrollY) + 'px';
        this.update.style.width = rect.width + 'px';
        this.update.style.zIndex = '1000';
    },

    show: function() {
        this.update.style.display = 'block';
        this.active = true;
    },

    hide: function() {
        this.update.style.display = 'none';
        this.active = false;
    },

    render: function() {
        var entries = this.update.querySelectorAll('li');
        for (var i = 0; i < entries.length; i++) {
            if (i === this.index) {
                entries[i].classList.add('selected');
            } else {
                entries[i].classList.remove('selected');
            }
        }
    },

    onKeyDown: function(e) {
        if (!this.active) return;

        switch (e.keyCode) {
            case 13: // Enter
                e.preventDefault();
                this.selectEntry();
                return;
            case 27: // Escape
                this.hide();
                this.active = false;
                return;
            case 38: // Up
                e.preventDefault();
                this.markPrevious();
                this.render();
                return;
            case 40: // Down
                e.preventDefault();
                this.markNext();
                this.render();
                return;
        }
    },

    onKeyUp: function(e) {
        // Ignore navigation keys
        if (e.keyCode === 13 || e.keyCode === 27 || e.keyCode === 38 || e.keyCode === 40) {
            return;
        }

        this.changed = false;

        if (this.getToken().length >= this.options.minChars) {
            // Debounce the request
            if (this.timeout) {
                clearTimeout(this.timeout);
            }
            var self = this;
            this.timeout = setTimeout(function() {
                self.getUpdatedChoices();
            }, this.options.frequency * 1000);
        } else {
            this.hide();
        }
    },

    onBlur: function(e) {
        this.hasFocus = false;
        this.hide();
    },

    onClick: function(e) {
        var target = e.target;
        while (target && target.tagName !== 'LI' && target !== this.update) {
            target = target.parentElement;
        }

        if (target && target.tagName === 'LI') {
            var entries = this.update.querySelectorAll('li');
            for (var i = 0; i < entries.length; i++) {
                if (entries[i] === target) {
                    this.index = i;
                    break;
                }
            }
            this.selectEntry();
        }
    },

    markPrevious: function() {
        if (this.index > 0) {
            this.index--;
        } else {
            this.index = this.entryCount - 1;
        }
    },

    markNext: function() {
        if (this.index < this.entryCount - 1) {
            this.index++;
        } else {
            this.index = 0;
        }
    },

    selectEntry: function() {
        this.active = false;
        this.updateElement(this.getCurrentEntry());
    },

    getCurrentEntry: function() {
        var entries = this.update.querySelectorAll('li');
        if (this.index >= 0 && this.index < entries.length) {
            return entries[this.index];
        }
        return null;
    },

    updateElement: function(selectedElement) {
        if (!selectedElement) return;

        this.changed = true;
        var value = this.options.array[this.index];
        if (typeof this.options.valueSelector === 'function') {
            value = this.options.valueSelector(value);
        }
        this.element.value = value;
        this.hide();

        // Call the afterUpdateElement callback if defined
        if (this.afterUpdateCallback) {
            this.afterUpdateCallback(this.element, selectedElement, this.options.array[this.index]);
        }

        this.element.focus();
    }
};