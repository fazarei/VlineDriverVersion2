/*
 *
 * Copyright (c) 1999-2011 NetDimensions Ltd.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * NetDimensions Ltd. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with NetDimensions.
 */
$(function() {

	$(".submenu:visible").hide(); //important for IE7

    $('.main-menu-container > ul > li > a').click(function(){
        $(".current").removeClass("current");
        $(this).addClass("current");
        while ($(".submenu").is(":visible")) {
        	$(".submenu").hide();
        }
        $(this).parent().children('.submenu').show();
    });
    
    $('.submenu > li > a').click(function(){
        $(".selected").removeClass("selected");
        $(this).addClass("selected");
    });
    

}); //end document.ready

function popup(url) {
    window.open(url, "_blank", "toolbar=no, menubar=no, resizable=yes, height=600, width=800");
}
