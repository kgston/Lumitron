require 'midilib'
require 'pp'
require_relative 'config.rb'

seq = MIDI::Sequence.new()
File.open(FILE_NAME, 'rb') { | file | seq.read(file) }

# load all NoteOn and NoteOff events into a stack
stack = []
seq.tracks[TRACK_ID].each do |event|
    if MIDI::NoteOn === event
        stack << [
            seq.pulses_to_seconds(event.time_from_start),
            event.note,
            :on
        ]
    end
    if MIDI::NoteOff === event
        stack << [
            seq.pulses_to_seconds(event.time_from_start),
            event.note,
            :off
        ]
    end
end

queue = stack.reverse

# start playing!
puts "Loaded #{queue.length} events from #{FILE_NAME}"
puts "The delay is set to #{DELAY_MSEC}."
print "Press ENTER to start playing..."
gets

# remember start time
time_start = Time.now

while queue.length > 0
    time_now = Time.now

    if time_now - time_start >= queue.last[0] - DELAY_MSEC
        event = queue.pop
        puts "ON: #{event[1]}"  if event[2] == :on
        puts "OFF: #{event[1]}" if event[2] == :on
    end

    Kernel::sleep(0.001)
end

puts "Finished playing."

# display total playing time
time_end = Time.now
puts "Playing time: #{time_end - time_start} seconds."