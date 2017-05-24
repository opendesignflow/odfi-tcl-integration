::nx::Class create A {
    :property -accessor public name 

}

puts "Created A"

set instance [::A new -name MyName]
puts "Created instance of A: $instance"