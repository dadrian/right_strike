//Builds the graph using data querried from the db
function build_graph(chart_data) {
  //Get the context of the canvas element we want to select
  if(chart_data) {
    var run_date = chart_data.date;
    var range = [];
    var run_step_count = chart_data.count;
    var run_steps = chart_data.data;
    var i;
    for (i = 0; i <= run_step_count; i++) {
      range[i] = i;
      if (run_steps[i] == "H") {
        run_steps[i] = 0;
      //  console.log("run_steps is: ");
      //  console.log(run_steps[i]);
      }
      else if (run_steps[i] == "F") {
        run_steps[i] = 1;
      //  console.log("run_steps is: ");
      //  console.log(run_steps[i]);
      }
    }
    console.log(run_steps);
    var running_average = [];
    running_average[0] = run_steps[0];
    for (i = 1; i <= run_step_count; i++) {
      running_average[i] = (run_steps[i] + (i-1)*running_average[i-1])/i;
      //console.log(run_steps[i]);
      //console.log(running_average[i]);
    }
    console.log(running_average);
    var ctx = document.getElementById("The_Chart").getContext("2d");
    var data2 = {
      labels : range,
      datasets : [
        {
          fillColor : "rgba(0,240,0,0.5)",
          strokeColor : "rgba(0,0,0,1)",
          pointColor : "rgba(0,0,0,1)",
          pointStrokeColor : "#fff",
          data : running_average
        }
        /*{
          fillColor : "rgba(0,240,0,.5)",
          strokeColor : "rgba(0,0,0,1)",
          pointColor : "rgba(0,0,0,1)",
          pointStrokeColor : "#fff",
          data : run_steps
        }*/
      ]
    };
    var options = {

      //String - Colour of the scale line 
      scaleLineColor : "rgba(0,0,0,1)",

      //Boolean - If we want to override with a hard coded scale
      scaleOverride : true,

      //Boolean - Whether the line is curved between points
      bezierCurve : true,

      //** Required if scaleOverride is true **
      //Number - The number of steps in a hard coded scale
      scaleSteps : 1,
      //Number - The value jump in the hard coded scale
      scaleStepWidth : 1,
      //Number - The scale starting value
      scaleStartValue : 0,

      //String - Colour of the grid lines
      scaleGridLineColor : "rgba(0,0,0,.01)",

      //Boolean - Whether to show labels on the scale 
      scaleShowLabels : false,

      //Interpolated JS string - can access value
      scaleLabel : "<strike>",

      //String - Scale label font declaration for the scale label
      scaleFontFamily : "'Sans-Serif'",

      //Boolean - Whether to show a dot for each point
      pointDot : false
    };

    var myNewChart = new Chart(ctx).Line(data2, options);
  }
}
