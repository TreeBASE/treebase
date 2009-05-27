	
	function queryStatus() {	
		RemoteAjaxProgressListener.getStatus(showStatus);
		return true;
	}
	
	function showStatus(status) {
		if (status == "100"){
			document.getElementById("progressBarText").style.visibility = 'hidden';
			document.getElementById("progressBarSuccessful").style.visibility = 'visible';
			document.getElementById("progressBarBoxContent").style.width = parseInt(status * 4) + "px";
		} else {
			
			document.getElementById("progressBarSuccessful").style.visibility = 'hidden';
			document.getElementById("progressBar").style.display = "block";
			document.getElementById("percentage").innerHTML= ' ' + status.percentage;
//		document.getElementById("bytesRead").innerHTML= ' ' + status.bytesRead;
//		document.getElementById("totalSize").innerHTML= ' ' + status.totalSize;
			document.getElementById("progressBarBoxContent").style.width = (status.percentage * 4) + "px";
			setTimeout(queryStatus, 2000);
		}
		return true;
	}