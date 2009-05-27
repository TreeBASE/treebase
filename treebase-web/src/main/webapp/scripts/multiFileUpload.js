<script type="text/javascript">
    var rowCount = 0;
    function addFile() {
      var table = document.getElementById('attachments');
      var tbody = table.getElementsByTagName("tbody")[0];
      
      var rows = tbody.getElementsByTagName("TR");
      var row = document.createElement("TR");
      var cell = document.createElement("TD");
      var input = document.createElement("INPUT"); 
      input.setAttribute("type","file");
      input.setAttribute("name","file["+rowCount+"]");
      input.setAttribute("size","40");
      cell.appendChild(input);
      
      var removeSpan = document.createElement("SPAN");
      removeSpan.innerHTML = '<a href="javascript:removeFile('+rowCount+')>remove</a>';
      cell.appendChild(removeSpan);
      
      row.setAttribute("id","file-"+rowCount);
      row.appendChild(cell);
      tbody.appendChild(row);
      rowCount++;
    }
    
    function removeFile(i) {
      var tbody = document.getElementById('attachments').getElementsByTagName("tbody")[0];
      tbody.removeChild(document.getElementById("file-"+i));
    } 
</script>