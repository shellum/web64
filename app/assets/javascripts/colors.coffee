init = ->
  $("#save").click(save)
  hideAlerts()
  $(".close").click(hideAlerts)
  $("#refresh").click(updateRandomColors)
  getAllColors()
  updateRandomColors()

hideAlerts = ->
  $(".alert").hide()

fadeAlerts = ->
  $(".alert").fadeOut(500)

showSaveConfirm = (data) ->
  $(".alert").show()
  setTimeout(fadeAlerts, 1000)
  getAllColors()
  updateRandomColors()

makeNewGradient = (e) ->
  newGradient = $("<div id='gradient" + e.key + "'/>")
  newGradient.css("background", "linear-gradient(120deg, " + e.startColor + " 30%, " + e.endColor + " 70%)")
  newGradient.css("height", "100px")
  newGradient.css("width", "300px")
  newGradient.css("margin", "auto")
  $("#allColors").append(newGradient)
  deleteButton = $("<span id='" + e.key + "' class='glyphicon glyphicon-remove'></span>")
  deleteButton.click(removeGradient)
  $("#gradient" + e.key).append(deleteButton)

removeGradient = (e) ->
  key = $(e.target).attr("id")
  $.ajax({type: "GET", url: "/gradientsRemove/" + key, success: readyToUpdateScreen})

readyToUpdateScreen = ->
  updateRandomColors()

showAllColors = (data) ->
  $("#allColors").empty()
  x = JSON.parse(data)
  x.forEach(makeNewGradient)

updateRandomColors = ->
  $.ajax({type: "GET", url: "/gradientsRandom", success: updateRandomColorsCallback})

updateRandomColorsCallback = (data) ->
  gradient = $('#gradient')
  gradient.css("background", "linear-gradient(120deg, " + data[0] + " 30%, " + data[1] + " 70%)")
  $("#startColor").val(data[0])
  $("#endColor").val(data[1])
  getAllColors()

save = ->
  $.ajax({type: "POST", url: "/gradientsSave", data: $("form").serialize(), success: showSaveConfirm})

getAllColors = ->
  $.ajax({type: "GET", url: "/gradientsGetAll", success: showAllColors})

$ init