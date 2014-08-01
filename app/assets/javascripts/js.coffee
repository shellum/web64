init = ->
  $("#encode").click(encode)
  $("#decode").click(decode)

updateText = (data) ->
  $("#data").val(data)

encode = ->
  $.post("/encode", { data: $("#data").val() }, updateText)

decode = ->
  $.post("/decode", { data: $("#data").val() }, updateText)

$ init