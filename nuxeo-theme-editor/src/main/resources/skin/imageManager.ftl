<div class="nxthemesThemeControlPanelScreen">

<#assign screen="image-manager" />

<div id="nxthemesImageManager" class="nxthemesThemeControlPanel">

<h1 class="nxthemesEditor">Choose an image</h1>

<table class="nxthemesManageScreen">
  <tr>
    <th style="width: 25%;">Theme bank</th>
    <th style="width: 75%;">Images</th>
  </tr>
  <tr>
  <td>

<div class="album">
  <#list images as image>
    <a href="javascript:void(0)"
       onclick="NXThemesImageManager.selectImage('${current_edit_field}', '${image}')">
      <div class="imageSingle">
        <div class="image"><img src="${selected_bank.connectionUrl}/${image}/image" /></div>
        <div class="footer">${image}</div>
      </div>
    </a>
  </#list>
</div>

<div
  <#if selected_bank>
    <iframe id="upload_target" name="upload_target" src="" style="display: none"></iframe>

     <h2 class="nxthemesEditor">Upload an image</h2>
     <form id="uploadImageForm" action="${selected_bank.connectionUrl}/manage/upload"
          enctype="multipart/form-data" method="post" target="upload_target">
      <p>
        <input type="file" name="file" size="30" />
        <input type="hidden" name="bank" value="${selected_bank.name}" />
        <input type="hidden" name="collection" value="custom" />
        <input type="hidden" name="redirect_url" value="${Root.getPath()}/imageUploaded" />
      </p>
      <p>
      <button>Upload</button>
      </p>
    </form>
    </#if>
</div>

</td>

<td>

</td>
</tr>
</table>


</div>
</div>