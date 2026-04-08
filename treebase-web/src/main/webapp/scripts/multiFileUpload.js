<script type="text/javascript">
    var rowCount = 0;
    function addFile() {
      var container = document.getElementById('attachments');
      
      var row = document.createElement("div");
      row.setAttribute("id", "file-" + rowCount);
      row.className = "d-flex align-items-center gap-2 mb-2";
      
      var input = document.createElement("input"); 
      input.setAttribute("type", "file");
      input.setAttribute("name", "file[" + rowCount + "]");
      input.className = "form-control form-control-sm";
      input.style.maxWidth = "400px";
      row.appendChild(input);
      
      var removeBtn = document.createElement("a");
      removeBtn.href = "javascript:removeFile(" + rowCount + ")";
      removeBtn.className = "btn btn-outline-danger btn-sm";
      removeBtn.innerHTML = '<i class="fa fa-times"></i> Remove';
      row.appendChild(removeBtn);
      
      container.appendChild(row);
      rowCount++;
    }
    
    function removeFile(i) {
      var container = document.getElementById('attachments');
      var row = document.getElementById("file-" + i);
      if (row) {
        container.removeChild(row);
      }
    } 
</script>