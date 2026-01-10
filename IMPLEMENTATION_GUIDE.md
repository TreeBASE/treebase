# TreeBASE UI Modernization - Implementation Guide

**Quick Start Guide for Developers**

This document provides practical, actionable steps to begin implementing the recommendations from `WEB_UI_ANALYSIS.md`.

---

## Quick Wins (Start Here)

These changes provide immediate value with minimal risk:

### 1. Fix Missing CSS File (5 minutes)

**Problem:** `menuExpandable2.css` is referenced but doesn't exist

**Solution:**
```bash
# Option 1: Create the file (copy from menuExpandable.css)
cp treebase-web/src/main/webapp/styles/menuExpandable.css \
   treebase-web/src/main/webapp/styles/menuExpandable2.css

# Option 2: Update references to use existing file
# Edit mainTemplate.jsp and testTemplate.jsp
# Change: menuExpandable2.css → menuExpandable.css
```

### 2. Create CSS Variables (30 minutes)

**File:** `treebase-web/src/main/webapp/styles/_variables.css`

```css
:root {
  /* Colors - extracted from current styles.css */
  --color-primary: #003366;
  --color-primary-light: #3863A4;
  --color-accent: #FF4500;
  --color-link: #003366;
  --color-link-visited: #3863a4;
  
  --color-bg-white: #FFFFFF;
  --color-bg-light: #f5f5f5;
  --color-bg-gray: #e8e8e8;
  
  --color-border: #CCC;
  --color-border-dark: #808080;
  
  /* Table colors */
  --table-bg-even: #d6cfe6;  /* Light purple */
  --table-bg-odd: #FFFFFF;
  --table-bg-header: silver;
  --table-bg-hover: #DDDDFF;
  
  /* Typography */
  --font-family-base: "Helvetica Neue", GillSans, helvetica, "lucida sans", arial, sans-serif;
  --font-family-heading: "Helvetica Neue", Arial, sans-serif;
  --font-family-serif: Georgia, "Times New Roman", Times, serif;
  
  --font-size-base: 80%;
  --line-height-base: 1.5em;
  
  /* Spacing */
  --spacing-xs: 0.25rem;
  --spacing-sm: 0.5rem;
  --spacing-md: 1rem;
  --spacing-lg: 1.5rem;
  --spacing-xl: 2rem;
  
  /* Layout */
  --content-width: 67%;
  --sidebar-width: 32%;
  --wrap-min-width: 500px;
  --wrap-max-width: 1800px;
  
  /* Z-index scale */
  --z-index-base: 1;
  --z-index-dropdown: 100;
  --z-index-modal: 1000;
  --z-index-tooltip: 1100;
}
```

**Add to templates:**
```jsp
<!-- In defaultTemplate.jsp, mainTemplate.jsp, etc. -->
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/_variables.css'/>" />
```

### 3. Create Utility Classes (1 hour)

**File:** `treebase-web/src/main/webapp/styles/_utilities.css`

```css
/* Display utilities */
.hidden { display: none !important; }
.block { display: block !important; }
.inline-block { display: inline-block !important; }
.flex { display: flex !important; }

/* Text alignment */
.text-left { text-align: left !important; }
.text-center { text-align: center !important; }
.text-right { text-align: right !important; }

/* Spacing utilities */
.m-0 { margin: 0 !important; }
.m-1 { margin: var(--spacing-sm) !important; }
.m-2 { margin: var(--spacing-md) !important; }

.p-0 { padding: 0 !important; }
.p-1 { padding: var(--spacing-sm) !important; }
.p-2 { padding: var(--spacing-md) !important; }

.mt-0 { margin-top: 0 !important; }
.mt-1 { margin-top: var(--spacing-sm) !important; }
.mt-2 { margin-top: var(--spacing-md) !important; }

.mb-0 { margin-bottom: 0 !important; }
.mb-1 { margin-bottom: var(--spacing-sm) !important; }
.mb-2 { margin-bottom: var(--spacing-md) !important; }

.pt-0 { padding-top: 0 !important; }
.pt-1 { padding-top: var(--spacing-sm) !important; }
.pt-2 { padding-top: var(--spacing-md) !important; }

.pb-0 { padding-bottom: 0 !important; }
.pb-1 { padding-bottom: var(--spacing-sm) !important; }
.pb-2 { padding-bottom: var(--spacing-md) !important; }

/* Width utilities */
.w-full { width: 100% !important; }
.w-half { width: 50% !important; }
.w-third { width: 33.333% !important; }

/* Vertical alignment */
.v-align-top { vertical-align: top !important; }
.v-align-middle { vertical-align: middle !important; }
.v-align-bottom { vertical-align: bottom !important; }
```

**Start replacing inline styles:**

```jsp
<!-- BEFORE -->
<div style="text-align:center">Content</div>
<input style="width:100%" type="text" />
<td style="vertical-align:middle">Cell</td>

<!-- AFTER -->
<div class="text-center">Content</div>
<input class="w-full" type="text" />
<td class="v-align-middle">Cell</td>
```

---

## Phase 1: CSS Consolidation (Weeks 1-4)

### Week 1: Setup and Planning

#### Day 1-2: Document Current Inline Styles

Create a script to find all inline styles:

```bash
#!/bin/bash
# save as: scripts/find-inline-styles.sh

echo "=== Inline Styles Report ==="
echo "Generated: $(date)"
echo ""

echo "Files with inline styles:"
grep -r "style=" treebase-web/src/main/webapp/WEB-INF/pages/*.jsp \
  | cut -d: -f1 \
  | sort -u \
  | wc -l

echo ""
echo "Top 10 most common inline styles:"
grep -roh 'style="[^"]*"' treebase-web/src/main/webapp/WEB-INF/pages/*.jsp \
  | sort \
  | uniq -c \
  | sort -rn \
  | head -10

echo ""
echo "Total inline style occurrences:"
grep -r "style=" treebase-web/src/main/webapp/WEB-INF/pages/*.jsp \
  | wc -l
```

#### Day 3-5: Create Base CSS Structure

```bash
# Create new CSS directory structure
mkdir -p treebase-web/src/main/webapp/styles/base
mkdir -p treebase-web/src/main/webapp/styles/components
mkdir -p treebase-web/src/main/webapp/styles/utilities
mkdir -p treebase-web/src/main/webapp/styles/layout

# Create base files
touch treebase-web/src/main/webapp/styles/base/_variables.css
touch treebase-web/src/main/webapp/styles/base/_reset.css
touch treebase-web/src/main/webapp/styles/base/_typography.css
touch treebase-web/src/main/webapp/styles/utilities/_utilities.css
```

**_reset.css:**
```css
/* Modern CSS Reset */
*, *::before, *::after {
  box-sizing: border-box;
}

* {
  margin: 0;
  padding: 0;
}

html, body {
  height: 100%;
}

body {
  line-height: 1.5;
  -webkit-font-smoothing: antialiased;
}

img, picture, video, canvas, svg {
  display: block;
  max-width: 100%;
}

input, button, textarea, select {
  font: inherit;
}

p, h1, h2, h3, h4, h5, h6 {
  overflow-wrap: break-word;
}
```

### Week 2: Component Extraction

#### Extract Table Styles

**File:** `treebase-web/src/main/webapp/styles/components/_tables.css`

```css
/* Unified table component */
.table {
  width: 100%;
  border-collapse: collapse;
  border: 1px solid var(--color-border-dark);
  background: var(--color-bg-white);
  margin-top: 10px;
}

.table th {
  background-color: var(--table-bg-header);
  padding: 0.5rem;
  border-bottom: 1px solid black;
  text-align: left;
  color: black;
  font-weight: 600;
}

.table td {
  padding: 0.5rem;
}

/* Striped tables */
.table--striped tbody tr:nth-child(even) {
  background: var(--table-bg-even);
  border-top: 1px solid silver;
}

.table--striped tbody tr:nth-child(odd) {
  background: var(--table-bg-odd);
  border-top: 1px solid silver;
}

/* Hover effect */
.table tbody tr:hover {
  background: var(--table-bg-hover);
  cursor: pointer;
}

/* Sortable tables */
.table--sortable th.sortable {
  cursor: pointer;
  user-select: none;
}

.table--sortable th.sortable:hover {
  background-color: #ffd;
}

/* Icon column */
.table .icon-column {
  width: 16px;
  white-space: nowrap;
}

.table .icon-column-header {
  background-color: pink;
}
```

**Migration Example:**

```jsp
<!-- BEFORE: Mixed styles -->
<display:table name="${studyList}" 
  class="list"
  cellspacing="3"
  cellpadding="3">
  
  <display:column property="id" 
    style="text-align:left: width 10%"/>
</display:table>

<!-- AFTER: Clean classes -->
<display:table name="${studyList}" 
  class="table table--striped table--sortable">
  
  <display:column property="id" 
    class="text-left w-10"/>
</display:table>
```

### Week 3-4: Systematic Inline Style Removal

Create a tracking spreadsheet:

| File | Inline Styles | Status | Notes |
|------|---------------|--------|-------|
| about.jsp | 1 | ✅ Done | Replaced with `.text-center` |
| addPersonForm.jsp | 4 | ⏳ In Progress | Text inputs need `.w-full` |
| algorithm.jsp | 20 | ⏱️ Pending | Complex page, needs review |
| ... | ... | ... | ... |

**Process per file:**

1. Identify inline styles
2. Map to utility class or create component class
3. Replace inline style with class
4. Test visually
5. Mark as complete

---

## Phase 2: JavaScript Modernization (Weeks 5-12)

### Week 5: Setup Build Tools

#### Install Node.js and npm

```bash
cd treebase-web
npm init -y
```

#### Install Development Dependencies

```bash
npm install --save-dev \
  webpack webpack-cli webpack-dev-server \
  @babel/core @babel/preset-env babel-loader \
  css-loader style-loader \
  eslint prettier \
  terser-webpack-plugin
```

#### Create webpack.config.js

```javascript
const path = require('path');
const TerserPlugin = require('terser-webpack-plugin');

module.exports = {
  mode: process.env.NODE_ENV === 'production' ? 'production' : 'development',
  
  entry: {
    main: './src/main/webapp/js/main.js',
    treeViewer: './src/main/webapp/js/treeViewer.js'
  },
  
  output: {
    filename: '[name].bundle.js',
    path: path.resolve(__dirname, 'src/main/webapp/dist/js'),
    clean: true
  },
  
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env']
          }
        }
      }
    ]
  },
  
  optimization: {
    minimize: true,
    minimizer: [new TerserPlugin()],
    splitChunks: {
      chunks: 'all'
    }
  },
  
  devtool: 'source-map'
};
```

#### Create package.json scripts

```json
{
  "scripts": {
    "build": "webpack --mode production",
    "dev": "webpack --mode development --watch",
    "lint": "eslint src/main/webapp/js/**/*.js",
    "format": "prettier --write src/main/webapp/js/**/*.js"
  }
}
```

### Week 6-7: Replace Prototype.js Core Functions

#### Create Compatibility Layer

**File:** `treebase-web/src/main/webapp/js/utils/dom.js`

```javascript
/**
 * Modern replacements for Prototype.js functions
 */

// $ replacement
export const $ = (id) => document.getElementById(id);

// $$ replacement
export const $$ = (selector) => Array.from(document.querySelectorAll(selector));

// Element.observe replacement
export const observe = (element, event, handler) => {
  element.addEventListener(event, handler);
};

// Element.update replacement
export const update = (element, content) => {
  element.innerHTML = content;
};

// Element class utilities
export const hasClass = (element, className) => {
  return element.classList.contains(className);
};

export const addClass = (element, className) => {
  element.classList.add(className);
};

export const removeClass = (element, className) => {
  element.classList.remove(className);
};

// Modern fetch wrapper (Ajax.Request replacement)
export async function request(url, options = {}) {
  const defaults = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  };
  
  const config = { ...defaults, ...options };
  
  try {
    const response = await fetch(url, config);
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return await response.json();
    }
    
    return await response.text();
  } catch (error) {
    console.error('Request failed:', error);
    throw error;
  }
}

// Event delegation helper
export function delegate(parent, selector, event, handler) {
  parent.addEventListener(event, (e) => {
    const target = e.target.closest(selector);
    if (target) {
      handler.call(target, e);
    }
  });
}
```

#### Refactor common.js

**Before (Prototype.js):**
```javascript
var inputs = $$('.textCell');
for ( var i = 0; i < inputs.length; i++ ) {
  if ( inputs[i] ) {
    inputs[i].observe('focus', function () {
      this.addClassName('focused');
      this.select();
    });
  }
}
```

**After (Modern JS):**
```javascript
import { $$, observe, addClass } from './utils/dom.js';

const inputs = $$('.textCell');
inputs.forEach(input => {
  observe(input, 'focus', function() {
    addClass(this, 'focused');
    this.select();
  });
});
```

### Week 8-9: Replace Scriptaculous Effects

**Create CSS Transitions:**

```css
/* File: styles/components/_transitions.css */

.fade-in {
  animation: fadeIn 0.3s ease-in;
}

.fade-out {
  animation: fadeOut 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes fadeOut {
  from { opacity: 1; }
  to { opacity: 0; }
}

.slide-down {
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    max-height: 0;
    opacity: 0;
  }
  to {
    max-height: 1000px;
    opacity: 1;
  }
}
```

**JavaScript Helper:**

```javascript
// File: js/utils/animate.js

export function fadeOut(element, duration = 300) {
  element.style.transition = `opacity ${duration}ms`;
  element.style.opacity = '0';
  
  setTimeout(() => {
    element.style.display = 'none';
  }, duration);
}

export function fadeIn(element, duration = 300) {
  element.style.display = '';
  element.style.opacity = '0';
  element.style.transition = `opacity ${duration}ms`;
  
  // Trigger reflow
  element.offsetHeight;
  
  element.style.opacity = '1';
}

export function toggle(element, displayType = 'block') {
  if (element.style.display === 'none') {
    fadeIn(element);
  } else {
    fadeOut(element);
  }
}
```

**Replace Effect calls:**

```javascript
// BEFORE
Effect.Fade('element', { duration: 0.5 });

// AFTER
import { fadeOut } from './utils/animate.js';
fadeOut(document.getElementById('element'), 500);
```

### Week 10-12: DWR Replacement Strategy

#### Step 1: Create REST Endpoints

**Backend (Java):**

```java
@RestController
@RequestMapping("/api")
public class PersonApiController {
    
    @Autowired
    private PersonService personService;
    
    @GetMapping("/person/email")
    public ResponseEntity<List<String>> findCompleteEmailAddress(
        @RequestParam String q
    ) {
        List<String> emails = personService.findEmailsByPrefix(q);
        return ResponseEntity.ok(emails);
    }
}
```

#### Step 2: Create JavaScript Service Layer

**File:** `js/services/personService.js`

```javascript
import { request } from '../utils/dom.js';

export class PersonService {
  static async findCompleteEmailAddress(query) {
    return await request(`/api/person/email?q=${encodeURIComponent(query)}`);
  }
}
```

#### Step 3: Update Autocomplete

**BEFORE (DWR):**
```javascript
function updateList(autocompleter, token) {
  RemotePersonService.findCompleteEmailAddress(token, function(data) {
    autocompleter.setChoices(data);
  });
}
```

**AFTER (Fetch):**
```javascript
import { PersonService } from './services/personService.js';

async function updateList(autocompleter, token) {
  try {
    const data = await PersonService.findCompleteEmailAddress(token);
    autocompleter.setChoices(data);
  } catch (error) {
    console.error('Failed to fetch email suggestions:', error);
  }
}
```

---

## Testing Strategy

### Visual Regression Testing

```bash
# Install BackstopJS
npm install --save-dev backstopjs

# Initialize
npx backstop init

# Configure scenarios in backstop.json
{
  "scenarios": [
    {
      "label": "Homepage",
      "url": "http://localhost:8080/treebase-web/home.html"
    },
    {
      "label": "Study List",
      "url": "http://localhost:8080/treebase-web/search/study/list.html"
    }
  ]
}

# Create reference
npx backstop reference

# After changes, test
npx backstop test
```

### Browser Testing Checklist

- [ ] Chrome (latest)
- [ ] Firefox (latest)
- [ ] Safari (latest)
- [ ] Edge (latest)
- [ ] Mobile Safari (iOS)
- [ ] Chrome Mobile (Android)

### Manual Testing Checklist

For each page with changes:

- [ ] Visual appearance matches original
- [ ] All interactive elements work
- [ ] Forms submit correctly
- [ ] Tables sort/paginate properly
- [ ] Navigation functions
- [ ] No console errors
- [ ] Responsive on mobile
- [ ] Keyboard navigation works
- [ ] Screen reader accessible

---

## Rollback Plan

### Keep Old Code During Transition

```jsp
<%-- defaultTemplate.jsp --%>

<c:set var="useModernCSS" value="${param.modern == 'true'}" />

<c:choose>
  <c:when test="${useModernCSS}">
    <%-- New consolidated CSS --%>
    <link rel="stylesheet" href="<c:url value='/styles/main.css'/>" />
  </c:when>
  <c:otherwise>
    <%-- Legacy CSS --%>
    <link rel="stylesheet" href="<c:url value='/styles/styles.css'/>" />
    <link rel="stylesheet" href="<c:url value='/styles/displaytag.css'/>" />
    <%-- ... other old CSS files ... --%>
  </c:otherwise>
</c:choose>
```

Enable with: `?modern=true` URL parameter

### Git Branches Strategy

```bash
# Feature branches for each component
git checkout -b feature/css-variables
git checkout -b feature/table-components
git checkout -b feature/js-utils
git checkout -b feature/remove-prototype

# Merge when stable
git checkout main
git merge feature/css-variables
```

---

## Monitoring and Metrics

### Performance Metrics

**Before Modernization:**
```bash
# Measure current performance
curl -w "@curl-format.txt" -o /dev/null -s http://localhost:8080/treebase-web/home.html
```

**curl-format.txt:**
```
time_namelookup:  %{time_namelookup}\n
time_connect:  %{time_connect}\n
time_appconnect:  %{time_appconnect}\n
time_pretransfer:  %{time_pretransfer}\n
time_redirect:  %{time_redirect}\n
time_starttransfer:  %{time_starttransfer}\n
----------\n
time_total:  %{time_total}\n
size_download:  %{size_download}\n
speed_download:  %{speed_download}\n
```

### Track Progress

Create `modernization-progress.md`:

```markdown
# Modernization Progress Tracker

## CSS Consolidation
- [ ] CSS variables created (0/1)
- [ ] Utility classes created (0/1)
- [ ] Inline styles removed (0/82 files)
- [ ] Component CSS extracted (0/10 components)

## JavaScript Modernization
- [ ] Prototype.js replaced (0/12 files)
- [ ] Scriptaculous replaced (0/5 files)
- [ ] DWR replaced (0/8 endpoints)
- [ ] Build system configured (0/1)

## Testing
- [ ] Visual regression tests (0/20 pages)
- [ ] Browser testing (0/6 browsers)
- [ ] Accessibility audit (0/1)

## Performance Goals
- Initial CSS size: 24KB → Target: 15KB
- Initial JS size: 300KB → Target: 100KB
- Page load time: Baseline → Target: -30%
```

---

## Getting Help

### Resources

**CSS:**
- [MDN CSS Reference](https://developer.mozilla.org/en-US/docs/Web/CSS)
- [CSS Tricks](https://css-tricks.com/)
- [Can I Use](https://caniuse.com/) - Browser support

**JavaScript:**
- [MDN JavaScript Guide](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide)
- [You Don't Need jQuery](https://github.com/nefe/You-Dont-Need-jQuery)
- [Modern JS Cheatsheet](https://mbeaudru.github.io/modern-js-cheatsheet/)

**Tools:**
- [Webpack Documentation](https://webpack.js.org/)
- [Babel Documentation](https://babeljs.io/docs/)
- [ESLint Rules](https://eslint.org/docs/rules/)

### Common Issues

**Problem:** Styles not applying after consolidation
**Solution:** Check specificity, ensure new CSS is loaded after old CSS

**Problem:** JavaScript errors after Prototype removal
**Solution:** Check browser console, likely missed dependency

**Problem:** Build failing
**Solution:** Check Node version (need 14+), clear node_modules and reinstall

---

## Next Steps

1. **Week 1:** 
   - Review this guide
   - Set up development environment
   - Create CSS variables and utilities

2. **Week 2-4:**
   - Extract component CSS
   - Begin replacing inline styles
   - Track progress

3. **Week 5:**
   - Set up build tools
   - Create JavaScript utilities

4. **Week 6-12:**
   - Systematic JS modernization
   - Replace libraries one by one
   - Test thoroughly

5. **Ongoing:**
   - Monitor performance
   - Gather user feedback
   - Iterate and improve

---

**Remember:** 
- Start small, test often
- Don't try to do everything at once
- Keep old code working during transition
- Document your changes
- Ask for help when stuck

Good luck! 🚀
