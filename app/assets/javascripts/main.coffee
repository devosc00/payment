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
        $('form').append("<input type='hidden' name='OpenPayu-Signature'>").attr("value", resp)
        $('form').submit
        console.log "success " + resp
      error: (resp) -> console.log "error" + resp

  $("#add").click ->
    product = []
    for material, index in $("#form").find("input[name^='products']")
      product.push material.value
    materialsArray.push product
    console.log materialsArray


#    startKIRPayment: (kirData) ->
#      form = $("<form>").prop("name", "payUForm").prop("method", "POST").prop("action", kirData.url).css("display", "none")
#      $("body").append form
#      transaction = $("<input>").prop("type", "hidden").prop("name", "hashtrans").prop("value", kirData.transactionHash)
#      form.append(transaction).submit()
