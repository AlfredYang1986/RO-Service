/**
 * Created by liwei on 2017/8/18.
 */
$(function () {
    loadFunction();
})

var loadFunction = function () {
    bootstraptab();
}

var bootstraptab = function() {
    var flagnext = false;
    var step = function(tab, navigation, index) {
        var $total = navigation.find('li').length;
        var $current = index+1;
        var $percent = ($current/$total) * 100;
        $('#progressWizard').find('.progress-bar').css('width', $percent+'%');
    }

    $('#progressWizard').bootstrapWizard({
        'nextSelector': '.next',
        'previousSelector': '.previous',
        onNext: function(tab, navigation, index) {
            try {
                if(index == 1){
                    step(tab, navigation, index);
                    setTimeout(function () {
                        $(".next").attr("class", "next")
                    },200)
                }
            } catch (err) {
                console.info(err.name);
            }
        },
        onPrevious: function(tab, navigation, index) {
            if(index >= 0) {
                flagnext = false;
                step(tab, navigation, index);
            }
        }
    });

    $('#disabledTabWizard').bootstrapWizard({
        tabClass: 'nav nav-pills nav-justified nav-disabled-click',
        onTabClick: function(tab, navigation, index) {
            return false;
        }
    });
}