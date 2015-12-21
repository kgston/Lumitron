var lumitron = lumitron || {};

lumitron.opts = $.extend(true, lumitron.opts || {}, (function() {
    return {
        debug: true,
        device: {
            search: {
                sequencialSearchCount: 10,
                timeout: 2000,
                fromIP: "10.10.1.1",
                toIP: "10.10.1.5"
            },
            heartbeat: {
                intervalLength: 5000
            }
        }
    };
})());

lumitron.project = $.extend(true, lumitron.project || {}, (function() {
    return {
        projectName: "Noukai 2015",
        music: {
            file: "C:\\Fresh.mp3"
        },
        devices: [{
            deviceName: "Taichi",
            macAddress: "ac-cf-23-8d-fe-7c",
            controller: "ZJ-200"
        }],
        events: [
            {
                "deviceName": "Taichi",
                "time": 4000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 4000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 4000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 4000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 4250,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 4250,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 4250,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 4250,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 4500,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 4500,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 4500,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 4500,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 4750,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 4750,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 4750,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 4750,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 5000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 5000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 5000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 5000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 5250,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 5250,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 5250,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 5250,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 5500,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 5500,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 5500,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 5500,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 5750,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 5750,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 5750,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 5750,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 6000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 6000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 6000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 6000,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 7000,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 7000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 7000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 7000,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 7250,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 7250,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 7250,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 7250,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 7500,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 7500,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 7500,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 7500,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 7750,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 7750,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 7750,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 7750,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 8000,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 8000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 8000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 8000,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 8250,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 8250,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 8250,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 8250,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 8500,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 8500,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 8500,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 8500,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 18000,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 18000,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 18000,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 18000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 26000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 26000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 26000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 26000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 26000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 26000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 26000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 26000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 35000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 35000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 35000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 35000,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 35000,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 35000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 35000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 35000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 37000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 37000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 37000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 37000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 37000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 37000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 38000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 38000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 38000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 38000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 38000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 38000,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 41000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 41000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 42000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 42000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 42000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 42000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 43000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 43000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 48250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 48250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 48500,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 48500,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 48750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 48750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 49000,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 49000,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 50000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 50000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 50250,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 50250,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 50500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 50500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 50750,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 50750,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 56000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 56000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 56000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 56000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 56000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 56000,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 62000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 62000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 62000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 62000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 79000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 79000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 79000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 79000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 79000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 79000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 80000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 80000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 80500,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 80500,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 80750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 80750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 81000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 81000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 81000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 81000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 81000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 81000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 81500,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 81500,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 81500,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 81500,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 81500,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 81500,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 87000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 87000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 87000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 87000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 87000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 87000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 95000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 95000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 95000,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 95000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 95000,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 95000,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 95000,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 95000,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 100000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 100000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 113000,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 113000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 123000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 123000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 123000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 123000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 123000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 123000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 123000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 123000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 123000,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 123000,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 123000,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 123000,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 123250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 123250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 123250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 123250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 123250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 123250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 123250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 123250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 123500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 123500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 123500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 123500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 123500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 123500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 123500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 123500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 123500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 123500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 123500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 123500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 123750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 123750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 123750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 123750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 123750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 123750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 123750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 123750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 123750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 123750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 123750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 123750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 124000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 124000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 124000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 124000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 124000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 124000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 124000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 124000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 124000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 124000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 124000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 124000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 124250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 124250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 124250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 124250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 124250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 124250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 124250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 124250,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 124250,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 124250,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 124250,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 124250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 124500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 124500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 124500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 124500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 124500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 124500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 124500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 124500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 124500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 124500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 124500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 124500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 124750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 124750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 124750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 124750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 124750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 124750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 124750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 124750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 124750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 124750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 124750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 124750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 125000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 125000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 125000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 125000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 125000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 125000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 125000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 125000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 125000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 125000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 125000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 125000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 125250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 125250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 125250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 125250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 125250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 125250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 125250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 125250,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 125250,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 125250,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 125250,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 125250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 125500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 125500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 125500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 125500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 125500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 125500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 125500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 125500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 125500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 125500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 125500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 125500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 125750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 125750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 125750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 125750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 125750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 125750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 125750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 125750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 125750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 125750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 125750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 125750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 126000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 126000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 126000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 126000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 126000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 126000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 126000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 126000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 126000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 126000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 126000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 126000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 126250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 126250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 126250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 126250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 126250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 126250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 126250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 126250,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 126250,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 126250,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 126250,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 126250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 126500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 126500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 126500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 126500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 126500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 126500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 126500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 126500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 126500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 126500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 126500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 126500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 126750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 126750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 126750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 126750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 126750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 126750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 126750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 126750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 126750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 126750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 126750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 126750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 127000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 127000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 127000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 127000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 127000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 127000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 127000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 127000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 127000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 127000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 127000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 127000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 127250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 127250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 127250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 127250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 127250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 127250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 127250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 127250,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 127250,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 127250,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 127250,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 127250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 127500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 127500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 127500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 127500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 127500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 127500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 127500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 127500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 127500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 127500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 127500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 127500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 127750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 127750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 127750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 127750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 127750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 127750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 127750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 127750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 127750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 127750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 127750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 127750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 128000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 128000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 128000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 128000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 128000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 128000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 128000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 128000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 128000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 128000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 128000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 128000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 128250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 128250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 128250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 128250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 128250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 128250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 128250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 128250,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 128250,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 128250,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 128250,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 128250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 128500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 128500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 128500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 128500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 128500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 128500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 128500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 128500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 128500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 128500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 128500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 128500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 128750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 128750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 128750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 128750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 128750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 128750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 128750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 128750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 128750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 128750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 128750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 128750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 129000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 129000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 129000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 129000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 129000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 129000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 129000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 129000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 129000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 129000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 129000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 129000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 129250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 129250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 129250,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 129250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 129250,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 129250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 129250,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 129250,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 129250,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 129250,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 129250,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 129250,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 129500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 129500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 129500,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 129500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 129500,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 129500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 129500,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 129500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 129500,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 129500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 129500,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 129500,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 129750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 129750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 129750,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 129750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 129750,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 129750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 129750,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 129750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 129750,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 129750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 129750,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 129750,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 137000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 137000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 137000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 137000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 137000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 137000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 137000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 137000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 137000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 137000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 137000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 137000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 137500,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 137500,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 137500,
                "command": "on"
            },
            {
                "deviceName": "Jumin",
                "time": 137500,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 137500,
                "command": "on"
            },
            {
                "deviceName": "Makimaki",
                "time": 137500,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 137500,
                "command": "on"
            },
            {
                "deviceName": "Natsuki",
                "time": 137500,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 137500,
                "command": "on"
            },
            {
                "deviceName": "Lupin",
                "time": 137500,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 137500,
                "command": "on"
            },
            {
                "deviceName": "Fujiko",
                "time": 137500,
                "command": "on"
            },
            {
                "deviceName": "Taichi",
                "time": 168000,
                "command": "off"
            },
            {
                "deviceName": "Taichi",
                "time": 168000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 168000,
                "command": "off"
            },
            {
                "deviceName": "Jumin",
                "time": 168000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 168000,
                "command": "off"
            },
            {
                "deviceName": "Makimaki",
                "time": 168000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 168000,
                "command": "off"
            },
            {
                "deviceName": "Natsuki",
                "time": 168000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 168000,
                "command": "off"
            },
            {
                "deviceName": "Lupin",
                "time": 168000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 168000,
                "command": "off"
            },
            {
                "deviceName": "Fujiko",
                "time": 168000,
                "command": "off"
            }
        ]
    };
})());

lumitron = $.extend(true, lumitron || {}, (function() {
	//Private vars
	
	//Private methods
	
	//Initializes the application and run all startup scripts
	var init = function() {
		return lumitron.ui.inlineSVG().done(function() {
		    lumitron.request.init();
            lumitron.device.init();
            lumitron.music.init();
            lumitron.events.init();
        });
	};
	
	//Util function to get a random UUID
	var getUUID = function() {
	    //Retrieved from http://stackoverflow.com/questions/105034/how-to-create-a-guid-uuid-in-javascript
        var d = new Date().getTime();
        if(window.performance && typeof window.performance.now === "function"){
            d += performance.now(); //use high-precision timer if available
        }
        var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = (d + Math.random()*16)%16 | 0;
            d = Math.floor(d/16);
            return (c=='x' ? r : (r&0x3|0x8)).toString(16);
        });
        return uuid;
	};
	
	//Public API
	return {
		init: init,
		getUUID: getUUID
	}
})());

lumitron.ui = $.extend(true, lumitron.ui || {}, (function() {
    //Converts all external SVGs into inline SVGs so they can be manipulated by CSS  
	var inlineSVG = function() {
        //http://stackoverflow.com/questions/11978995/how-to-change-color-of-svg-image-using-css-jquery-svg-image-replacement
        var inlining = $.Deferred();
		$('img.svg').each(function(){
            var $img = jQuery(this);
            var imgID = $img.attr('id');
            var imgClass = $img.attr('class');
            var imgURL = $img.attr('src');

            $.get(imgURL, function(data) {
                // Get the SVG tag, ignore the rest
                var $svg = jQuery(data).find('svg');
                // Add replaced image's ID to the new SVG
                if(typeof imgID !== 'undefined') {
                    $svg = $svg.attr('id', imgID);
                }
                // Add replaced image's classes to the new SVG
                if(typeof imgClass !== 'undefined') {
                    $svg = $svg.attr('class', imgClass+' replaced-svg');
                }
                // Remove any invalid XML tags as per http://validator.w3.org
                $svg = $svg.removeAttr('xmlns:a');
                // Replace image with new SVG
                $img.replaceWith($svg);
                //And wrap it with an outer div
                $svg.wrap(document.createElement("div"));
            }, 'xml').done(function() {
                if($('img.svg').length === 0) {
                    inlining.resolve();
                }
            });
        });
        return inlining.promise();
	};
    
    //Set the changeSVGSrc method
    $.fn.changeSVGSrc = function(newSrc) {
        $.get(newSrc, function(data) {
                // Get the SVG tag, ignore the rest
                var $svg = jQuery(data).find('svg');
                //Replace contents of the existing svg with the new svg contents
                this.empty().append($svg.children());
            }.bind(this), 'xml');
        return this;
    };
    //Set the SVG icon click method
    $.fn.iconClick = function(callback) {
        return this.each(function(index, element) {
            var icon = $(this);
            var parentDiv = icon.parent();
            parentDiv.click(callback.bind(parentDiv));
        });
    };
    //Set the SVG icon off method
    $.fn.iconOff = function(eventName) {
        return this.each(function(index, element) {
            var icon = $(this);
            var parentDiv = icon.parent();
            parentDiv.off(eventName);
        });
    };
    //Set the SVG icon addClass method
    $.fn.iconAddClass = function(classnames) {
        return this.each(function(index, element) {
            var classnameAry = classnames.split(" ");
            classnameAry.forEach(function(classname) {
                this.classList.add(classname);
            }.bind(this));
        });
    };
    //Set the SVG icon removeClass method
    $.fn.iconRemoveClass = function(classnames) {
        return this.each(function(index, element) {
            var classnameAry = classnames.split(" ");
            classnameAry.forEach(function(classname) {
                this.classList.remove(classname);
            }.bind(this));
        });
    };
    //Set the inputComplete method
    $.fn.inputComplete = function(callback) {
        return this.each(function(index, element) {
            var input = $(this);
            input.bind('blur keyup',function(event) {  
                if(event.type == 'blur' || event.keyCode == 13) {
                    callback.bind(this)(event);
                }
            }); 
        });
    };
    
    return {
        inlineSVG: inlineSVG
    };
})());

$(document).ready(function() {
	lumitron.init();
});