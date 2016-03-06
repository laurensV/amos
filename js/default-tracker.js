window.sessionId = -1;
api_url = "http://iavconcepts.com:7070"
$(document).ready(function() {
    var requestUrl = api_url + "/dna/init/" + window.site_id;
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        async: false,
        xhrFields: {
              withCredentials: true
        },
        success: function(data) {
            console.log(data);
            //window.sessionId = data.session_id;
            $.each(data.items, function(i, item) {
                $.each(item.attributes, function(i, attr) {
                    $(item.id).css(attr.attribute, attr.value);
                });
                // $(item.id).click(function() {
                //     var requestUrl = api_url + "/dna/click/" + window.sessionId;
                //     $.ajax({
                //         url: requestUrl,
                //         dataType: 'json',
                //         xhrFields: {
                //             withCredentials: true
                //         },
                //         success: function(data) {
                //             console.log(data);
                //         }
                //     });
                // });
            });
        },
        error: function(xhr, textStatus, errorThrown){
           alert('init request failed');
        }
    });
}); 
$(window).load(function() {
    var requestUrl = api_url + "/dna/start/" + window.site_id;
    $.ajax({
        url: requestUrl,
        dataType: 'json',
        xhrFields: {
              withCredentials: true
        },
        success: function(data) {
            console.log(data);
        },
        error: function(xhr, textStatus, errorThrown){
           alert('start request failed');
        }
    });
});
$(window).unload(function(){
    var requestUrl = api_url + "/dna/stop/" + window.site_id;
    /* synchronous call, so browser has to wait for call to finish before exiting */
    $.ajax({
        async:false,
        url: requestUrl,
        dataType: 'json',
        xhrFields: {
              withCredentials: true
        },
        success: function(data) {
            console.log(data);
        },
        error: function(xhr, textStatus, errorThrown){
           alert('stop request failed');
        }
    });
});
window.site_id = 