(function(){
    	var winWidth =  $(window).width();
    	var id = document.getElementById("card-set1");
    	if (winWidth >= 1154)
    		id.style.padding="0% 1% 0% 12.5%";
    	else if (winWidth >= 992 && winWidth <= 1154)
    	{
    		id.style.padding="0% 1% 0% 5%";
    		console.log(winWidth);
    	}
    	else if (winWidth > 767 && winWidth < 992)
    	{
    		id.style.padding="0% 1% 0% 13%";
    		console.log(winWidth);
    	}
    })();

    

    $('#navbarToggle').click('hidden.bs.collapse', function ()
	{
		var isHidden = $('.collapse').is(':visible');
		if(isHidden)
		{
			$('#navbarToggle').blur();
			$('.navbar-toggle').css("background-color","fff");
		}
	});
