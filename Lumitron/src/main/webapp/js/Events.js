var lumitron = lumitron || {};

lumitron.events = (function() {
    var eventsTable;
    
    var registerEvents = function() {
        var events = lumitron.project.events;
        var params = {
            "events": JSON.stringify(events)
        }
        return lumitron.request.send("ledevents", "play", params);
    };
    
    var init = function() {
        var events = lumitron.project.events;
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
            this.addRowSet()
                .addRow()
                .addRowColumn(event.time)
                .addRowColumn(event.deviceName)
                .addRowColumn(event.command)
                .addRowColumn(JSON.stringify(event.params));
        }.bind(eventsTable));
        
        eventsTable.render(function(eventsTableFragment) {
            $("#eventsTable").replaceWith(eventsTableFragment);
        });
    };
    
    $(document).ready(function() {
        var events = lumitron.project.events;
        var increment = 250;
        var counter = 0;
        for(var i=100; i<=20000; i+=increment) {
            if(counter % 2 == 0) {
                events.push({"time": i,   "deviceName": "Test1", "command": "setStrobe", "params": {"colour": "FF0000"}});
            } else {
                events.push({"time": i,   "deviceName": "Test1", "command": "setStrobe", "params": {"colour": "0000FF"}});
            }
            counter++;
        }
        init();
    });
    
    return {
        registerEvents: registerEvents
    };
})();