var lumitron = lumitron || {};

lumitron.events = (function() {
    var eventsTable;
    
    var registerEvents = function() {
        var events = lumitron.project.events;
        var params = {
            "events": JSON.stringify(events),
            "verbose": false
        };
        return lumitron.request.send("ledevents", "play", params);
    };
    
    var init = function() {
        var events = lumitron.project.events;
        stencilTemplates.init("table").done(function() {
            eventsTable = stencilTemplates.table("eventsTable");
            eventsTable
                .enableSort(true, [0, 1, 2])
                .enablePaginate(13)
                .enableSearch()
                
                .addHeaderRow(null, "deviceTitle whiteGlowFont tableHeader")
                .addHeaderColumn("Time", "")
                .addHeaderColumn("Device")
                .addHeaderColumn("Event")
                .addHeaderColumn("Value");
                
            events.forEach(function(event) {
                this.addRowSet();
                Object.keys(event.params).forEach(function(paramKey, index) {
                        this.addRow();
                    if(index === 0) {
                        this.addRowColumn(toTime(event.time))
                            .addRowColumn(event.deviceName)
                            .addRowColumn(event.command);
                    } else {
                        this.addRowColumn()
                            .addRowColumn()
                            .addRowColumn();
                    }
                    this.addRowColumn(paramKey + " -> " + event.params[paramKey]);
                }.bind(this));
            }.bind(eventsTable));
            
            eventsTable.render(function(eventsTableFragment) {
                $("#eventsTable").replaceWith(eventsTableFragment);
            });
        });
    };
    
    $(document).ready(function() {
        //var events = lumitron.project.events;
        // var increment = 250;
        // var counter = 0;
        // for(var i=100; i<=20000; i+=increment) {
        //     if(counter % 2 == 0) {
        //         events.push({"time": i,   "deviceName": "Test1", "command": "setStrobe", "params": {"colour": "FF0000"}});
        //     } else {
        //         events.push({"time": i,   "deviceName": "Test1", "command": "setStrobe", "params": {"colour": "0000FF"}});
        //     }
        //     counter++;
        // }
    });
    
    return {
        registerEvents: registerEvents,
        init: init
    };
    
    function toTime(timeInMs) {
        var ms = timeInMs % 1000;
        var seconds = Math.floor(timeInMs / 1000 % 60);
        var minutes = Math.floor(timeInMs / 1000 / 60 % 60);
        var secondsString = seconds.toString();
        if(secondsString.length == 1) {
            secondsString = "0" + secondsString;
        }
        var msString = ms.toString();
        while(msString.length < 3) {
            msString = "0" + msString;
        }
        return minutes + ":" + secondsString + ":" + msString;
    }
})();