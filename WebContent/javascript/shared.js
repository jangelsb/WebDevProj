$(document).ready(function() {

    $('.buyButton').click( function(e) {

        $.ajax({
            url : 'ProcessShoppingCart',
            data : "id="+this.id,
            success : function(responseText) {

             if(responseText === "false")
             {             
                 console.log("Failed to load");
             }

            }
        });
         
        var cart = $('#shoppingCartBtn');
        var imgtodrag = $(this).parent().find("img").eq(0);

        cartCounter++;

        if (imgtodrag) {
            var imgclone = imgtodrag.clone()
                .offset({
                top: imgtodrag.offset().top,
                left: imgtodrag.offset().left
            })
                .css({
                'opacity': '0.5',
                    'position': 'absolute',
                    'height': '150px',
                    'width': '100px',
                    'z-index': '9000'
            })
                .appendTo($('html'))
                .animate({
                'top': cart.offset().top + 10,
                    'left': cart.offset().left + 10,
                    'width': 50,
                    'height': 75
            }, 500, 'easeInOutExpo');

            imgclone.animate({
                'width': 0,
                    'height': 0
            }, function () {
                $(this).detach()
            });
            
            //checkForCount
            if (cartCounter < 0) {
                $('#shoppingCartCounter').hide();
            }
            else {
                $('#shoppingCartCounter').show();
            }
            
            $('#shoppingCartCounter').text(String(cartCounter));
        }
            
        e.preventDefault();
            
    });

    $('#byTitle').click( function() {
                
        toggleTitle = !toggleTitle;
        if(toggleTitle)
            {
                reload(offset, limit, m_title, 'asc_t', year, director, fName, lName, adv);
            }
        else
            {
                reload(offset, limit, m_title, 'desc_t', year, director, fName, lName, adv);
            }
    });

    $('#byYear').click( function() {
                
        toggleYear = !toggleYear;
        if(toggleYear)
            {
            	reload(offset, limit, m_title, 'asc_y', year, director, fName, lName, adv);
            }
        else
            {
           		reload(offset, limit, m_title, 'desc_y', year, director, fName, lName, adv);
            }
    });
    
    var toggleTitle = false;
    var toggleYear = false;
    
    if(orderby === "asc_y")
    {
        toggleYear = true;
        $('#yearArrow').removeClass("arrow-flat").addClass("arrow-up");
    }
    else if(orderby === "desc_y") 
    {
        $('#yearArrow').removeClass("arrow-flat").addClass("arrow-down");
    }
    

    if(orderby === "asc_t")
    {
        toggleTitle = true;
        $('#titleArrow').removeClass("arrow-flat").addClass("arrow-up");
    }
    else if(orderby === "desc_t")
    {
        $('#titleArrow').removeClass("arrow-flat").addClass("arrow-down");
    }
    

    $('#shoppingCartCounter').text(String(cartCounter));

    if (cartCounter < 1) 
    {
       $('#shoppingCartCounter').hide();      
    }
    else 
    {
        $('#shoppingCartCounter').show();
    }       

});

function createCookie(name, value, days) {

	if (days) {
		var date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		var expires = "; expires=" + date.toGMTString();
	} else
		var expires = "";
	document.cookie = name + "=" + value + expires + "; path=/";
}

function eraseCookie(name) {

	createCookie(name, "", -1);
}

function readCookie(name) {

	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ')
			c = c.substring(1, c.length);
		if (c.indexOf(nameEQ) == 0)
			return c.substring(nameEQ.length, c.length);
	}
	return null;
}
