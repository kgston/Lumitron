var lumitron = lumitron || {};

lumitron.events = (function() {
    var eventsTable;
    
    var init = function() {
        eventsTable = stencilTemplates.table("eventsTable");
        eventsTable
            .enableSort(true, [0, 1, 2])
            .enablePaginate(20)
            .enableSearch()
            
            .addHeaderRow(null, "deviceTitle whiteGlowFont tableHeader")
            .addHeaderColumn("Time", "")
            .addHeaderColumn("Device")
            .addHeaderColumn("Event")
            .addHeaderColumn("Value");
            
            for(var i=0; i<50; i++) {
                eventsTable
                    .addRowSet()
                    .addRow()
                    .addRowColumn("0:10")
                    .addRowColumn("Device " + i)
                    .addRowColumn("Set Colour")
                    .addRowColumn("#FF00FF");
            }
            
            eventsTable.render(function(eventsTableFragment) {
                $("#eventsTable").replaceWith(eventsTableFragment);
            });
    };
    
    $(document).ready(function() {
        init();
    });
    
    return {
        
    };
})();