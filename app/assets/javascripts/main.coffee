$ ->
  materialsArray = []

  $("#but").click ->
    serverRoutes.controllers.Application.sendJsonOrder().ajax
    contentType: "application/json"
    success: (resp) ->
      console.log resp.url + " " + resp.order
#        #        json = JSON.parse(resp)
#        $.ajax
#          contentType: "application/json"
#          method: "post"
#          url: resp.url
#          data: resp.order
#          headers: { "Authorization" : "Basic " + resp.header }
#          resp.header
#          console.log resp.header
#          success: (data) ->
#            console.log data
#          error: (data) ->
#            console.log data
    error: (resp) ->
      console.log "error" + resp



  $("#but").click ->
    serverRoutes.controllers.Application.sendDataToFormBuilder().ajax
      contentType: "application/json"
      data: JSON.stringify
        form: materialsArray
        console.log "send clicked"
      success: (resp) ->
        form = $("<form>").prop("name", "paymentForm").prop("method", "POST").prop("action", "https://secure.payu.com/api/v2_1/orders").css("display", "none")
        $("body").append form
        formBody = resp.form
        transaction = $("<input>").prop("type", "hidden").prop("name", "OpenPayu-Signature").prop("value", resp.signature)
        transId = $("<input>").prop("type", "hidden").prop("name", "extOrderId").prop("value", "PGlkX2NsaWVudD43ODExMDk1NDIzPC9pZF9jbGllbnQ+PGlkX3RyYW5zPjAwMDAwMDAzMTM8L2lkX3RyYW5zPjxkYXRlX3ZhbGlkPjI1LTA2LTIwMTUgMTI6MzQ6MDQ8L2RhdGVfdmFsaWQ+PGFtb3VudD40Miw3MDwvYW1vdW50PjxjdXJyZW5jeT5QTE48L2N1cnJlbmN5PjxlbWFpbD48L2VtYWlsPjxhY2NvdW50Pjk0OTA0MzEwNTQ2NTE5ODExNDAyMjUwNzA0PC9hY2NvdW50PjxhY2NuYW1lPlN5c3RoZXJtLUluZm8gU0tMRVAgVGVzdG93eV5OTV42MC0xODleWlBeUG96bmFuXkNJXnVsLiBabG90b3dza2EgMjdeU1ReUG9sc2thXkNUXjwvYWNjbmFtZT48YmFja3BhZ2U+aHR0cDovLzc3LjY1LjE4LjM0OjgwODIvcGxhdG5vc2Mvc3VrY2VzPC9iYWNrcGFnZT48YmFja3BhZ2VyZWplY3Q+aHR0cDovLzc3LjY1LjE4LjM0OjgwODIvcGxhdG5vc2MvcG9yYXprYTwvYmFja3BhZ2VyZWplY3Q+PGhhc2g+Mjg1NzE3YTY0ZGQ0OGUwNTFhNDBjNWRkMWZjOTE1NjkxNzIzY2QxZDwvaGFzaD4=")
        form.append(formBody).append(transId).append(transaction).submit()
        console.log "success " + resp.form + "  " + resp.signature
      error: (resp) -> console.log "error" + resp

  $("#add").click ->
    product = []
    for material, index in $("#form").find("input[name^='products']")
      product.push material.value
    materialsArray.push product
    console.log materialsArray

  $("#changePassword").change ->
    newPassword = $('<input type="password" id="newPassword" name="newPassword">').addClass("form-control").wrap('<div class="col-sm-9"></div>').parent()
    newPasswordRepeate = $('<input type="password" id="newPassword" name="newPassword">').addClass("form-control").wrap('<div class="col-sm-9"></div>').parent()
    .append('<span class="help-block">Hasła muszą być takie same</span>')
    newPasswordBlock = $('<label for="newPassword" class="col-sm-3 control-label">Nowe hasło</label>').wrap('<div class="form-group"></div>').parent().append(newPassword)
    newPasswordRepeateBlock = $('<label for="newPasswordrepeate" class="col-sm-3 control-label">Powtórz nowe hasło</label>').wrap('<div class="form-group"></div>').parent().append(newPasswordRepeate)
    $("#formGroup").append(newPasswordBlock).append(newPasswordRepeateBlock)


