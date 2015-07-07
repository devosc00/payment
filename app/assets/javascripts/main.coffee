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
    addChangePasswordBlock: ->
      newPassword = $('<input type="password" id="newPassword" name="newPassword">').addClass("form-control").wrap('<div class="col-sm-9"></div>').parent()
      newPasswordRepeate = $('<input type="password" id="newPasswordRepeat" name="newPasswordRepeat">').addClass("form-control").wrap('<div class="col-sm-9"></div>').parent()
      .append('<span class="help-block hidden">Hasła muszą być takie same</span>')
      newPasswordBlock = $('<label for="newPassword" class="col-sm-3 control-label">Nowe hasło</label>').wrap('<div class="form-group hidden"></div>').parent().append(newPassword)
      newPasswordRepeateBlock = $('<label for="newPasswordRepeat" class="col-sm-3 control-label">Powtórz nowe hasło</label>').wrap('<div class="form-group hidden"></div>').parent().append(newPasswordRepeate)
      $("#formGroup").append(newPasswordBlock).append(newPasswordRepeateBlock) if $("#formGroup").children().length == 4

  $("#changePassword").change ->
    $("#changePassword").slice(-2).remove()

