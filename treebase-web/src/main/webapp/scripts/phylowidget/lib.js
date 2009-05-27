var PhyloWidget = {

/**
 *  The following functions provide the preferred way of creating PhyloWidget applets.
 * 
 * See the index.html for examples of how to embed PhyloWidget.
 */
 codebase: 'lib',
 newwindow: '',
 
 useFull: function()
 {
 	PhyloWidget.codebase='http://' + top.location.host + '/nexml/java/jars/';
 },
 
 useLite: function()
 {
 	PhyloWidget.codebase="http://www.phylowidget.org/lite/lib";
 },
 
 remotePopup: function(version,params)
 {
 	// the 'version' parameter should be either 'full' or 'lite'.
 	//var url = 'http://www.phylowidget.org/'+version+'/bare.html?';
 	var url = 'bare.html?';
 	if (!params)
 		var params = {width:500,height:500};
 	url += PhyloWidget.getParamString(params);
	var width = params.width || 500;
	var height = params.height || 500;
	window.open(url,'PW','height='+height+',width='+width+",menubar=no,toolbar=no,location=no");
 },
 
/**
 * Loads PhyloWidget into the div with the given "id" attribute.
 */
loadWidget: function(dest_div,params)
{
	if (!params)						// Create a blank object if no params were given.
		var params = {};
	var dP = PhyloWidget.defaultParams();
	PhyloWidget.getQueryParameters(dP);		// Load the query parameters into the dP object.
	AppletLoader.mergeObjects(dP,params);		// Load the user-given parameters into the dP object.
	AppletLoader.loadApplet(dest_div,dP);	// Load up the applet.
},

/**
 * Loads a slick floating panel.
 */
loadWidgetPanel: function(params)
{
	if (!params)						// Create a blank object if no params were given.
		var params = {};
	var dP = PhyloWidget.defaultParams();
	PhyloWidget.getQueryParameters(dP);		// Load the query parameters into the dP object.
	AppletLoader.mergeObjects(dP,params);		// Load the user-given parameters into the dP object.
	AppletLoader.loadExtPanel(dP);
},

/**
 * Immediately writes the widget directly into the HTML document.
 */
writeWidget: function(params)
{
	if (!params)							// Create a blank object if no params were given.
		var params = {};
	var dP = PhyloWidget.defaultParams();
	PhyloWidget.getQueryParameters(dP);		// Load the query parameters into the dP object.
	AppletLoader.mergeObjects(dP,params);		// Load the user-given parameters into the dP object.
	AppletLoader.writeApplet(dP);
},
 
defaultParams: function()
{
	var defaultParams = {
		name:'PhyloWidget',
		code:'org.freeloader.FreeLoader',
		codebase:PhyloWidget.codebase,
		archive:'core.jar,freeloader.jar,itext.jar,pdf.jar,phylowidget.jar',
		cache_archive:'core.jar,freeloader.jar,itext.jar,pdf.jar,phylowidget.jar',
		cache_archive_ex:'core.jar,freeloader.jar,itext.jar,pdf.jar,phylowidget.jar',
		FreeLoader_loadingMessage: 'Loading PhyloWidget...',
		FreeLoader_mainClass: 'org.phylowidget.PhyloWidget',
		width: '500',
		height: '500',
		bgcolor: '#FFFFFF',					// Background color. Used by Java AND Javascript.
		fgcolor: '#3399CC',
		FreeLoader_minimumLoadingTime: '0'
	};
	return(defaultParams);
},
 
getWidth: function()
{
	var params = PhyloWidget.defaultParams();
	PhyloWidget.getQueryParameters(params);
	if (params['width'])
		return parseInt(params['width']);
	else
		return 500;
},

getHeight: function()
{
	var params = PhyloWidget.defaultParams();
	PhyloWidget.getQueryParameters(params);
	if (params['height'])
		return parseInt(params['height']);
	else
		return 500;
},
 
/**
 * Loads the toolbox into the given HTML element.
 */
loadToolbox: function(dest_id)
{
	PhyloWidget.toolboxID = dest_id;
       getObject(dest_id).innerHTML += "" +
       "<h1>Tree:</h1>" +
       "<div class='indent'>" + this.getTextFieldHTML(this.treeInputID,"PhyloWidget.updateJavaTree()","Clickaway to update the PhyloWidget tree.") + "</div>"+
       "<h1>Clipboard:</h1>" +
       "<div class='indent'>"+ this.getTextFieldHTML(this.clipInputID,"PhyloWidget.updateJavaClip()","Clickaway to update the PhyloWidget clipboard.") + "</div>" +
       "<h1>Node Info:</h1>" +
       "<div class='indent' id='"+this.nodeInfoID+"' style='height:100px;font-size:10px;'></div>" +
       "<div id='nodeInfoFooter' style=''></div>"
       ;
},

toolboxID:"",

toggleToolbox: function()
{
	var el = getObject(PhyloWidget.toolboxID);
	if (el == null)
		return;
	if (el.style.display=="none")
	{
		el.style.display="";
		text = getObject("hideshow");
		text.innerHTML = "hide";
	} else
	{
		el.style.display="none";
		text = getObject("hideshow");
		text.innerHTML = "show";
	}
},

isToolboxVisible: function()
{
	var el = getObject(PhyloWidget.toolboxID);
	if (el == null)
		return;
	if (el.style.display=="none")
		return false;
	else
		return true;
},

//useFull: function()
//{
//	AppletLoader.setCodebase("http://www.phylowidget.org/full/");
//},
//
//useLite: function()
//{
//	AppletLoader.setCodebase("http://www.phylowidget.org/lite/");
//},

getTextFieldHTML: function(id,fnToCall,msg)
{
   return "<textarea rows='3' cols='40' style='font-size:10px;' id='"+id+"' onblur='javascript:"+fnToCall+";PhyloWidget.updateFooter(\"\");' onfocus='javascript:PhyloWidget.updateFooter(\""+msg+"\");'></textarea>";
},
 
changeSetting: function(setting,value)
{
	AppletLoader.callAppletMethod("changeSetting",setting,value);
},

changeSettings: function(newSettingsObject)
{
	for (var key in newSettingsObject)
	{
		var param = newSettingsObject[key];
		AppletLoader.callAppletMethod("changeSetting",key,param);
	}
},

callMethod: function(methodName)
{
	AppletLoader.callAppletMethod("callMethod",methodName);
},

// The ID of the tree input element.
treeInputID:"treeText",
// The ID of the clipboard input element.
clipInputID:"clipText",
// The ID of the node info element.
nodeInfoID:"nodeText",
	
updateTree: function(newtext)
{
	if (!PhyloWidget.isToolboxVisible())
		return;
  	setTimeout(function() {
		var el = getObject(PhyloWidget.treeInputID);
		if (el != null)
			el.value = newtext;
	},100);
},

updateClip: function(newtext)
{
	if (!PhyloWidget.isToolboxVisible())
		return;
	setTimeout(function() {
		var el = getObject(PhyloWidget.clipInputID);
		if (el != null)
			el.value = newtext;
	},100);
  return;
},

updateNode: function(newtext)
{
	if (!PhyloWidget.isToolboxVisible())
		return;
	setTimeout(function() {
		var el = getObject(PhyloWidget.nodeInfoID);
  		if (el != null)
  			el.innerHTML = newtext;
	},200);
},

updateFooter: function(text) {
	getObject("nodeInfoFooter").innerHTML = text;
},

/*
 * This function calls Java's updateTree method to update PhyloWidget's
 * representation of the tree.
 */
updateJavaTree: function()
{
	var value = getObject(this.treeInputID).value;
	AppletLoader.callAppletMethod("updateTree",value);
//	document.PhyloWidget.updateTree(getObject(this.treeInputID).value);
},

/*
*  This function calls Java's updateClip method to update PhyloWidget's
 *  tree clipboard.
 */
updateJavaClip: function()
{
  var value = getObject(this.clipInputID).value;
  AppletLoader.callAppletMethod("updateClip",value);
//  document.PhyloWidget.updateClip(getObject(clipInputID).value);
},

getQueryParameters: function(destObject,url)
{
	if (!destObject)
		var destObject = {};
	url = url || top.location.href;
	
	/*
	 * Scrape the tree text using a regular expression.
	 * 
	 * This avoids problems when trees muck up with the query parsing (usually when NHX contains the '&' character)...
	 */ 
	 var treePattern = /tree=(.*?)[;&]/;
	 var result = url.match(treePattern);
	 if (result != null)
	 {
	 	destObject['tree'] = unescape(result[1]).replace(/["']/g,"");
	 	url = url.replace(treePattern,"");
	 }
	 
	 /*
	  * Now load up the rest of the query params as normal.
	  */
	 destObject = AppletLoader.getQueryParameters(destObject,url);
	 return destObject;
},

getParamString: function(params)
{
	var string = '';
	for (var key in params)
	{
		string += key + "=";
		string += params[key] + "&";
	}
	return string;
}
};

// Wrapper function to get an object from the DOM via its ID attribute.
function getObject(id){
	var el = document.getElementById(id);
	return (el);
};

/*
 *  This function causes the newick input box to be selected fully once.
 */
var selected = false;
function selectOnce(el)
{
  if (selected)return;
  el.select();
  selected = true;
}

/**
 * MINIFIED appletloader.js, from FreeLoader project
 * http://www.andrewberman.org/projects/preload/
 */

function pulpcore_getCookie(name){name=name+"=";var i;if(document.cookie.substring(0,name.length)==name){i=name.length;}
else{i=document.cookie.indexOf('; '+name);if(i==-1){return null;}
i+=name.length+2;}
var endIndex=document.cookie.indexOf('; ',i);if(endIndex==-1){endIndex=document.cookie.length;}
return unescape(document.cookie.substring(i,endIndex));}
function pulpcore_setCookie(name,value,expireDate,path,domain,secure){var expires=new Date();if(expireDate===null){expires.setTime(expires.getTime()+(24*60*60*1000)*90);}
else{expires.setTime(expireDate);}
document.cookie=name+"="+escape(value)+"; expires="+expires.toGMTString()+
((path)?"; path="+path:"")+
((domain)?"; domain="+domain:"")+
((secure)?"; secure":"");}
function pulpcore_deleteCookie(name,path,domain){document.cookie=name+"="+
((path)?"; path="+path:"")+
((domain)?"; domain="+domain:"")+"; expires=Thu, 01-Jan-70 00:00:01 GMT";}
function pulpcore_appletLoaded(){pulpCoreObject.hideSplash();setTimeout(pulpCoreObject.showObject,50);}
var pulpCoreObject={requiredJRE:"1.4",ieRequiredJRE:"1,4,0,0",getJavaCAB:"http://java.sun.com/update/1.6.0/jinstall-6-windows-i586.cab",getJavaXPI:"http://java.sun.com/update/1.6.0/jre-6-windows-i586-jc.xpi",getJavaURL:"http://java.sun.com/webapps/getjava/BrowserRedirect?host=java.com"+'&returnPage='+document.location,appletHTML:"",appletInserted:false,getCodeBase:function(){var codeBase=document.URL;if(codeBase.length<=7||codeBase.substr(0,7)!="http://"){return"";}
if(codeBase.charAt(codeBase.length-1)!='/'){var index=codeBase.lastIndexOf('/');if(index>7){codeBase=codeBase.substring(0,index+1);}
else{codeBase+='/';}}
return codeBase;},write:function(params){document.write(pulpCoreObject.getObjectHTML(params));},hideSplash:function(){},showObject:function(){},splashLoaded:function(splash){pulpCoreObject.insertApplet();},insertApplet:function(){},getObjectHTML:function(params){pulpCoreObject.appletHTML="";pulpCoreObject.detectBrowser();if(!pulpCoreObject.isAcceptableJRE()){return pulpCoreObject.installLatestJRE();}
var splashHTML;if(params===null)
params={};var code=params['code']||window.pulpcore_class||"pulpcore.platform.applet.CoreApplet.class";var width=params['width']||window.pulpcore_width||640;var height=params['height']||window.pulpcore_height||480;var archive=params['archive']||window.pulpcore_archive||"project.jar";var bgcolor=params['bgcolor']||window.pulpcore_bgcolor||"#000000";var fgcolor=params['fgcolor']||window.pulpcore_fgcolor||"#aaaaaa";var codebase=params['codebase']||window.pulpcore_codebase||pulpCoreObject.getCodeBase();delete params['codebase'];delete params['code'];delete params['width'];delete params['height'];delete params['archive'];delete params['bgcolor'];delete params['fgcolor'];var objectParams='  <param name="code" value="'+code+'" />\n'+'  <param name="archive" value="'+archive+'" />\n'+'  <param name="boxbgcolor" value="'+bgcolor+'" />\n'+'  <param name="boxfgcolor" value="'+fgcolor+'" />\n'+'  <param name="boxmessage" value="" />\n'+'  <param name="browsername" value="'+pulpCoreObject.browserName+'" />\n'+'  <param name="browserversion" value="'+pulpCoreObject.browserVersion+'" />\n'+'  <param name="java_arguments" value="-Dsun.awt.noerasebackground=true" />\n';if(codebase.length>0){objectParams+='  <param name="codebase" value="'+codebase+'" />\n';}
for(var i in params){objectParams+='  <param name="'+i+'" value="'+params[i]+'" />\n';}
objectParams+='  <param name="mayscript" value="true" />\n'+'  <param name="scriptable" value="true" />\n'+'  '+pulpCoreObject.getInstallHTML();if(pulpCoreObject.browserName=="Explorer"){var extraAttributes='';if(pulpCoreObject.compareVersions(pulpCoreObject.browserVersion,"7")<0&&parent.frames.length>0)
{extraAttributes='  onfocus="pulpcore_appletLoaded();"\n';}
pulpCoreObject.appletHTML='<object id="pulpcore_object"\n'+'  classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"\n'+'  codebase="'+pulpCoreObject.getJavaCAB+'#Version='+
pulpCoreObject.ieRequiredJRE+'"\n'+
extraAttributes+'  width="'+width+'" height="'+height+'">\n'+
objectParams+'</object>';splashHTML='<div id="applet_div" '+'style="position: relative; left: -3000px;"'+'></div>\n';}
else{if((pulpCoreObject.osName=="Windows"&&pulpCoreObject.browserName=="Safari"&&pulpCoreObject.compareVersions(pulpCoreObject.browserVersion,"522.11")>=0))
{pulpCoreObject.appletHTML='<applet id="pulpcore_object"\n'+'  codebase="'+codebase+'"\n'+'  code="'+code+'"\n'+'  archive="'+archive+'"\n'+'  width="'+width+'"\n'+'  height="'+height+'" mayscript="true">\n'+
objectParams+'</applet>';}
else{pulpCoreObject.appletHTML='<object id="pulpcore_object"\n'+'  classid="java:'+code+'"\n'+'  type="application/x-java-applet;version='+pulpCoreObject.requiredJRE+'"\n'+'  width="'+width+'" height="'+height+'">\n'+
objectParams+'</object>';}
var spacer="";if(pulpCoreObject.browserIsMozillaFamily){spacer='<div id="pulpcore_spacer" style="height: 100%">&nbsp;</div>\n';}
splashHTML=spacer+'<div id="applet_div" style="visibility: hidden"></div>\n';}
setTimeout(pulpCoreObject.insertApplet,1000);return'<div id="pulpcore_holder" style='+'"margin: 0px;padding:0px; '+'overflow: hidden; text-align: left; '+'width: '+width+'px; height: '+height+'px; '+'background: '+bgcolor+'">\n'+
pulpCoreObject.appletHTML+'</div>\n';},isAcceptableJRE:function(){var version;if(pulpCoreObject.browserName=="Explorer"){return true;}
else if(pulpCoreObject.browserName=="Safari"&&pulpCoreObject.osName=="Windows"){return true;}
else if(pulpCoreObject.browserName=="Safari"&&navigator.plugins&&navigator.plugins.length)
{for(var i=0;i<navigator.plugins.length;i++){var s=navigator.plugins[i].description;if(s.search(/^Java Switchable Plug-in/)!=-1){return true;}
var m=s.match(/^Java (1\.4\.2|1\.5|1\.6|1\.7).* Plug-in/);if(m!==null){version=m[1];if(pulpCoreObject.isAcceptableJREVersion(version)){return true;}}}
return false;}
else if(navigator.mimeTypes&&navigator.mimeTypes.length&&pulpCoreObject.browserIsMozillaFamily)
{if(pulpcore_getCookie("javaRecentlyInstalled")=="true"){return true;}
version=pulpCoreObject.getHighestInstalledJavaViaMimeTypes();return pulpCoreObject.isAcceptableJREVersion(version);}
else{return true;}},getHighestInstalledJavaViaMimeTypes:function(){var version="0.0";var mimeType="application/x-java-applet;version=";for(var i=0;i<navigator.mimeTypes.length;i++){var s=navigator.mimeTypes[i].type;if(s.substr(0,mimeType.length)==mimeType){var testVersion=s.substr(mimeType.length);if(pulpCoreObject.compareVersions(testVersion,version)==1){version=testVersion;}}}
return version;},isAcceptableJREVersion:function(version){var result=pulpCoreObject.compareVersions(version,pulpCoreObject.requiredJRE);return(result>=0);},compareVersions:function(versionA,versionB){versionA+='';versionB+='';var a=versionA.split('.');var b=versionB.split('.');var len=Math.max(a.length,b.length);for(var i=0;i<len;i++){if(i>=a.length){a[i]=0;}
if(i>=b.length){b[i]=0;}
if(a[i]>b[i]){return 1;}
if(a[i]<b[i]){return-1;}}
return 0;},installLatestJRE:function(){if(pulpCoreObject.shouldInstallXPI()){pulpCoreObject.installXPI();}
return pulpCoreObject.getInstallHTML();},getInstallHTML:function(){var extraAttributes='';if(pulpCoreObject.shouldInstallXPI()){extraAttributes=' onclick="pulpCoreObject.installXPI();return false;"';}
return'<p id="pulpcore_install" style="text-align: center">To play, '+'<a href="'+pulpCoreObject.getJavaURL+'"'+extraAttributes+'>'+'install Java now</a>.</p>\n';},shouldInstallXPI:function(){return pulpCoreObject.browserIsMozillaFamily&&pulpCoreObject.osName=="Windows"&&InstallTrigger&&InstallTrigger.enabled();},installXPI:function(){var xpi={"Java Plug-in":pulpCoreObject.getJavaXPI};InstallTrigger.install(xpi,pulpCoreObject.installXPIComplete);},installXPIComplete:function(url,result){var success=(result===0);if(success){document.cookie="javaRecentlyInstalled=true; path=/";var version=pulpCoreObject.getHighestInstalledJavaViaMimeTypes().split('.');if(version[0]=="1"&&version[1]=="3"){var install=document.getElementById('pulpcore_install');install.innerHTML="Java installed! To play, you may need to restart your browser.";}
else{location.href=document.location;}}},versionSearchString:"",browserName:"",browserVersion:"",browserIsMozillaFamily:false,osName:"",detectBrowser:function(){pulpCoreObject.browserName=pulpCoreObject.searchString(pulpCoreObject.dataBrowser)||"An unknown browser";pulpCoreObject.browserVersion=pulpCoreObject.searchVersion(navigator.userAgent)||pulpCoreObject.searchVersion(navigator.appVersion)||"an unknown version";pulpCoreObject.osName=pulpCoreObject.searchString(pulpCoreObject.dataOS)||"an unknown OS";pulpCoreObject.browserIsMozillaFamily=pulpCoreObject.browserName=="Netscape"||pulpCoreObject.browserName=="Mozilla"||pulpCoreObject.browserName=="Firefox";},searchString:function(data){for(var i=0;i<data.length;i++){var dataString=data[i].string;var dataProp=data[i].prop;pulpCoreObject.versionSearchString=data[i].versionSearch||data[i].identity;if(dataString){if(dataString.indexOf(data[i].subString)!=-1){return data[i].identity;}}
else if(dataProp){return data[i].identity;}}},searchVersion:function(dataString){var index=dataString.indexOf(pulpCoreObject.versionSearchString);if(index==-1){return;}
return parseFloat(dataString.substring(index+pulpCoreObject.versionSearchString.length+1));},dataBrowser:[{string:navigator.userAgent,subString:"OmniWeb",versionSearch:"OmniWeb/",identity:"OmniWeb"},{string:navigator.vendor,subString:"Apple",identity:"Safari"},{prop:window.opera,identity:"Opera"},{string:navigator.vendor,subString:"iCab",identity:"iCab"},{string:navigator.vendor,subString:"KDE",identity:"Konqueror"},{string:navigator.userAgent,subString:"Firefox",identity:"Firefox"},{string:navigator.vendor,subString:"Camino",identity:"Camino"},{string:navigator.userAgent,subString:"Netscape",identity:"Netscape"},{string:navigator.userAgent,subString:"MSIE",identity:"Explorer",versionSearch:"MSIE"},{string:navigator.userAgent,subString:"Gecko",identity:"Mozilla",versionSearch:"rv"},{string:navigator.userAgent,subString:"Mozilla",identity:"Netscape",versionSearch:"Mozilla"}],dataOS:[{string:navigator.platform,subString:"Win",identity:"Windows"},{string:navigator.platform,subString:"Mac",identity:"Mac"},{string:navigator.platform,subString:"Linux",identity:"Linux"}]};var AppletLoader={lastAppletName:'',myApplets:{},curWindow:window,defaultParams:function()
{var defaultParams={name:'FreeLoader',code:'org.freeloader.FreeLoader',codebase:'lib',archive:'freeloader.jar',FreeLoader_loadingMessage:'Loading Java Applet...',mayscript:'true',image:'loading.gif',progressbar:'true',boxmessage:'Loading Java Applet...',boxbgcolor:'#FFFFFF',bgcolor:'#FFFFFF',fgcolor:'#3399CC',FreeLoader_minimumLoadingTime:'100'};return defaultParams;},storeName:function(params)
{AppletLoader.lastAppletName=params['name']||'';},loadApplet:function(dest_div,params)
{var defaults=AppletLoader.defaultParams();var myParams=AppletLoader.mergeObjects(defaults,params);AppletLoader.storeName(myParams);try{AppletLoader.stopApplet(myParams);}catch(err){}
AppletLoader.curWindow.document.getElementById(dest_div).innerHTML=pulpCoreObject.getObjectHTML(myParams);pulpCoreObject.showObject();},loadExtPanel:function(params)
{var defaults=AppletLoader.defaultParams();var myParams=AppletLoader.mergeObjects(defaults,params);AppletLoader.storeName(myParams);try{Ext;}catch(e){alert("You need to have properly setup Ext to load an Applet panel like this!");return;}
Ext.onReady(function(){var width;var height;if(myParams['width']!==null)
width=parseInt(myParams['width'])+20;else
width=400+30;if(myParams['height']!==null)
height=parseInt(myParams['height'])+35;else
height=400+30;var win=new Ext.Window({title:myParams['name']||'Java Applet',closable:true,width:width,height:height,plain:true,items:{html:pulpCoreObject.getObjectHTML(myParams),border:false}});win.on('close',function(){AppletLoader.stopApplet(myParams);AppletLoader.myApplets['applet_holder']=null;});win.on('show',function(){pulpCoreObject.showObject();});win.on('resize',function(obj,width,height){var holder=AppletLoader.curWindow.document.getElementById("pulpcore_holder");if(holder!==null)
{Ext.DomHelper.applyStyles(holder,{width:width-30,height:height-40});}
var obj=AppletLoader.curWindow.document.getElementById("pulpcore_object");if(obj!==null)
{obj.width=width-30;obj.height=height-40;}
var applet=AppletLoader.curWindow.document.applets[0];if(applet)
{applet.width=width-30;applet.height=height-40;}});AppletLoader.myApplets['applet_holder']=win;win.show();});},writeApplet:function(params)
{var defaults=AppletLoader.defaultParams();var myParams=AppletLoader.mergeObjects(defaults,params);pulpCoreObject.write(myParams);},stopApplet:function(params)
{if(params===null)
params={};var myName='pulpcore_object';var applet=AppletLoader.curWindow.document.getElementById(myName);if(applet!=null)
{applet.stop();applet.destroy();}},callAppletMethod:function(methodName,parameters){var myName='pulpcore_object';var applet=AppletLoader.curWindow.document.getElementById(myName);if(applet!=null)
{var splitter=applet.PARAM_SPLIT;var paramS='';for(var i=1,len=arguments.length;i<len;i++)
{if(i>1)
paramS+=splitter;paramS+=arguments[i];}
if(paramS.length==0)
var returnValue=applet.callMethod(methodName);else
var returnValue=applet.callMethod(methodName,paramS);return returnValue;}else{setTimeout(function(){AppletLoader.callAppletMethod(arguments);},200);}},mergeObjects:function(loadInto,loadFrom){if(loadInto===null)
var loadInto={};if(loadFrom===null)
var loadFrom={};for(var key in loadFrom)
{loadInto[key]=loadFrom[key];}
return loadInto;},getQueryParameters:function(destObject,url){if(!destObject)
var destObject={};var i,len,idx,queryString,params,tokens;url=url||top.location.href;idx=url.indexOf("?");queryString=idx>=0?url.substr(idx+1):url;idx=queryString.lastIndexOf("#");queryString=idx>=0?queryString.substr(0,idx):queryString;params=queryString.split("&");var obj={};for(var i=0,len=params.length;i<len;i++){tokens=params[i].split("=");if(tokens.length>=2){var key=unescape(tokens[0]).replace(/["']/g,"");var val=unescape(tokens[1]).replace(/["']/g,"");obj[key]=val;}}
AppletLoader.mergeObjects(destObject,obj);return destObject;}};