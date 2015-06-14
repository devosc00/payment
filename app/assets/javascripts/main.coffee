$ ->

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

#  $("#send").click ->
#    serverRoutes.controllers.Application.sendForm().ajax
#      contentType: "application/json"
#      data: $("#form").serialize()
#      success: (resp) ->
#        $('form').append("<input type='hidden' name='OpenPayu-Signature'>").attr("value", resp)
#        $('form').submit
#        console.log "success" + resp
#      error: (resp) -> console.log "error" + resp
