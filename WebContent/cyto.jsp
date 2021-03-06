<html>
<head>
<title>Insert title here</title>
<script src='cytoscape.js'></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
</head>
<style>
#cy {
	width: 100%;
	height: 100%;
	position: absolute;
	top: 0px;
	left: 0px;
}
</style>
<body>
	<div id="cy"></div>
	<script>

    
  //  ${requestScope.message}
	function getParam(variable)
	{
		var query=window.location.search.substring(1);
		var vars=query.split("&");
		for(var i=0;i<vars.length;i++)
		{
			var pair=vars[i].split("=");
			if(pair[0]==variable)
			{
				return pair[1];	
			}
		}
	}

	var tag=getParam("name");

	var tag2 = tag.replace("/", "replacementforbackslash");

	$.getJSON( "./rest/json/"+tag2, function( data ) {	

	
	var mess=getParam("param");
	var display;

	if(mess=="betweeness")
		{
		display='data(betweeness)';
		}
	if(mess=="tag")
	{
	display='data(fulltag)';
	}
	if(mess=="pagerank")
	{
	display='data(nodeRank)';
	}
	var cyt =cytoscape({
    container: document.getElementById('cy'),
    elements: data,
		    style: [
		        {
		            selector: 'node',
		            style: {
		                shape: 'hexagon',
		                'background-color': 'red',
		                label: display
		            }
		        }
		        ,
		        {
		         selector: '.father',
		         style: {
		         'background-color': 'blue',
		         'shape': 'triangle',
		         label: display
		        }
		        },
		        {
		         selector: '.child',
		         style: {
		         'background-color': 'green',
		         'shape': 'rectangle',
		         label: display
		        	 }
		        },
	            {
	                selector: 'edge',
	                style: {
	                	'curve-style': 'bezier',
                        'target-arrow-shape': 'triangle',
                        'target-arrow-color': 'black'
	                }
		        }],
		        layout: { name: 'preset',

					  positions: undefined, // map of (node id) => (position obj); or function(node){ return somPos; }
					  zoom: undefined, // the zoom level to set (prob want fit = false if set)
					  pan: undefined, // the pan level to set (prob want fit = false if set)
					  fit: true, // whether to fit to viewport
					  padding: 30, // padding on fit
					  animate: false, // whether to transition the node positions
					  animationDuration: 500, // duration of animation in ms if enabled
					  animationEasing: undefined, // easing of animation if enabled
					  ready: undefined, // callback on layoutready
					  stop: undefined // callback on layoutstop
					  },
		     // initial viewport state:
		        zoom: 1,
		        pan: { x: 0, y: 0 },

		        // interaction options:
		        minZoom: 1e-50,
		        maxZoom: 1e50,
		        zoomingEnabled: true,
		        userZoomingEnabled: true,
		        panningEnabled: true,
		        userPanningEnabled: true,
		        boxSelectionEnabled: false,
		        selectionType: 'single',
		        touchTapThreshold: 8,
		        desktopTapThreshold: 4,
		        autolock: false,
		        autoungrabify: false,
		        autounselectify: false,

		        // rendering options:
		        headless: false,
		        styleEnabled: true,
		        hideEdgesOnViewport: false,
		        hideLabelsOnViewport: false,
		        textureOnViewport: false,
		        motionBlur: false,
		        motionBlurOpacity: 0.2,
		        wheelSensitivity: 1,
		        pixelRatio: 'auto'
  });
	var options4 = {
			  name: 'random',

			  fit: true, // whether to fit to viewport
			  padding: 30, // fit padding
			  boundingBox: undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
			  animate: false, // whether to transition the node positions
			  animationDuration: 500, // duration of animation in ms if enabled
			  animationEasing: undefined, // easing of animation if enabled
			  ready: undefined, // callback on layoutready
			  stop: undefined // callback on layoutstop
			};
	var options3 = {
			  name: 'concentric',

			  fit: true, // whether to fit the viewport to the graph
			  padding: 30, // the padding on fit
			  startAngle: 3 / 2 * Math.PI, 
			  sweep: undefined, // how many radians should be between the first and last node (defaults to full circle)
			  clockwise: true, // whether the layout should go clockwise (true) or counterclockwise/anticlockwise (false)
			  equidistant: false, // whether levels have an equal radial distance betwen them, may cause bounding box overflow
			  minNodeSpacing: 10, // min spacing between outside of nodes (used for radius adjustment)
			  boundingBox: undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
			  avoidOverlap: true, // prevents node overlap, may overflow boundingBox if not enough space
			  nodeDimensionsIncludeLabels: false, // Excludes the label when calculating node bounding boxes for the layout algorithm
			  height: undefined, // height of layout area (overrides container height)
			  width: undefined, // width of layout area (overrides container width)
			  spacingFactor: undefined, // Applies a multiplicative factor (>0) to expand or compress the overall area that the nodes take up
			  concentric: function( node ){ // returns numeric value for each node, placing higher nodes in levels towards the centre
			  return node.degree();
			  },
			  levelWidth: function( nodes ){ // the variation of concentric values in each level
			  return nodes.maxDegree() / 4;
			  },
			  animate: false, // whether to transition the node positions
			  animationDuration: 500, // duration of animation in ms if enabled
			  animationEasing: undefined, // easing of animation if enabled
			  ready: undefined, // callback on layoutready
			  stop: undefined // callback on layoutstop
			};

	var options2 = {
			  name: 'breadthfirst',

			  fit: true, // whether to fit the viewport to the graph
			  directed: false, // whether the tree is directed downwards (or edges can point in any direction if false)
			  padding: 30, // padding on fit
			  circle: false, // put depths in concentric circles if true, put depths top down if false
			  spacingFactor: 1.75, // positive spacing factor, larger => more space between nodes (N.B. n/a if causes overlap)
			  boundingBox: undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
			  avoidOverlap: false, // prevents node overlap, may overflow boundingBox if not enough space
			  roots: undefined, // the roots of the trees
			  maximalAdjustments: 0, // how many times to try to position the nodes in a maximal way (i.e. no backtracking)
			  animate: false, // whether to transition the node positions
			  animationDuration: 500, // duration of animation in ms if enabled
			  animationEasing: undefined, // easing of animation if enabled
			  ready: undefined, // callback on layoutready
			  stop: undefined // callback on layoutstop
			};
	var options = {
			  name: 'preset',

			  positions: undefined, // map of (node id) => (position obj); or function(node){ return somPos; }
			  zoom: undefined, // the zoom level to set (prob want fit = false if set)
			  pan: undefined, // the pan level to set (prob want fit = false if set)
			  fit: true, // whether to fit to viewport
			  padding: 30, // padding on fit
			  animate: false, // whether to transition the node positions
			  animationDuration: 500, // duration of animation in ms if enabled
			  animationEasing: undefined, // easing of animation if enabled
			  ready: undefined, // callback on layoutready
			  stop: undefined // callback on layoutstop
			};
		//	cyt.layout( options4 );
			cyt.nodes().forEach(function( ele ){
				  if(ele.data('type')=="father")
					  {
					  var id=ele.id();
					  cyt.$('#'+id).classes('father');
					  }
				  if(ele.data('type')=="child")
				  {
				  var id=ele.id();
				  cyt.$('#'+id).classes('child');
				  }
				});
	cyt.on('tap', 'node',function(event){
		  // cyTarget holds a reference to the originator
		  // of the event (core or element)
		  var evtTarget = event.cyTarget;
		  
		  if( evtTarget === cyt ){
		      console.log('tap on background');
		  } else {
		    console.log('tap on some element');
		    console.log(evtTarget.data('tag'));
		    window.location = "/rest/res/"+encodeURIComponent(evtTarget.data('user'))+"/" + encodeURIComponent(evtTarget.data('name'))+"/" + encodeURIComponent(evtTarget.data('tag'));

		  }
		});
	});
</script>
</body>
</html>