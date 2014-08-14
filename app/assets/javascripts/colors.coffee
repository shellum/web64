init = ->
  $("#save").click(save)
  hideAlerts()
  $(".close").click(hideAlerts)
  getAllColors()

hideAlerts = ->
  $(".alert").hide()

fadeAlerts = ->
  $(".alert").fadeOut(500)

showSaveConfirm = (data) ->
  $(".alert").show()
  setTimeout(fadeAlerts, 1000)

makeNewGradient = (e) ->
  newGradient = $("<div class='gradient'/>")
  newGradient.css("background","linear-gradient(120deg, "+e.startColor+ " 30%, "+e.endColor+" 70%)")
  newGradient.css("height","100px")
  newGradient.css("width","300px")
  newGradient.css("margin","auto")
  $("#allColors").append(newGradient)

showAllColors = (data) ->
  $("#allColors").empty()
  x = JSON.parse(data)
  x.forEach(makeNewGradient)

updateRandomColors = ->
  $.ajax( {type: "GET", url: "/colorRandomGradient", success: updateRandomColorsCallback})

updateRandomColorsCallback = (data) ->
  gradient = $('#gradient')
  gradient.css("background","linear-gradient(120deg, "+data[0]+ " 30%, "+data[1]+" 70%)")

save = ->
  $.ajax( {type: "POST", url: "/colorSave", data: $("form").serialize(),success: showSaveConfirm})
  getAllColors()
  updateRandomColors()

getAllColors = ->
  $.ajax( {type: "GET", url: "/colorGetAll", success: showAllColors})

$ init