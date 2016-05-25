window.sessionId = -1;
api_url = "http://localhost:7070"
$(document).ready(function() {
    var requestUrl = api_url + "/dna/init/" + window.site_id;
    $.ajax({
        url: requestUrl,
        dataType: 'json',
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
                $(item.id).click(function() {
                    goal("click-"+$(this).attr('id'), 10)

                });
            });
            goal("visit", 1);
        },
        error: function(xhr, textStatus, errorThrown){
           console.log('init request failed');
        },
        timeout: 2000 // sets timeout to 3 seconds
    });
});
function goal(name, score){
    var requestUrl = api_url + "/dna/goal/" + name + "/" +score;
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
           console.log('goal request failed');
        },
        timeout: 2000 // sets timeout to 3 seconds
    });    
}
$(window).load(function() {
    // var requestUrl = api_url + "/dna/start/" + window.site_id;
    // $.ajax({
    //     url: requestUrl,
    //     dataType: 'json',
    //     xhrFields: {
    //           withCredentials: true
    //     },
    //     success: function(data) {
    //         console.log(data);
    //     },
    //     error: function(xhr, textStatus, errorThrown){
    //        alert('start request failed');
    //     }
    // });
});
$(window).unload(function(){
    // var requestUrl = api_url + "/dna/stop/" + window.site_id;
    // /* synchronous call, so browser has to wait for call to finish before exiting */
    // $.ajax({
    //     async:false,
    //     url: requestUrl,
    //     dataType: 'json',
    //     xhrFields: {
    //           withCredentials: true
    //     },
    //     success: function(data) {
    //         console.log(data);
    //     },
    //     error: function(xhr, textStatus, errorThrown){
    //        alert('stop request failed');
    //     }
    // });
});
window.site_id = 