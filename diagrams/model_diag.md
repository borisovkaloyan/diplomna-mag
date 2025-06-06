classDiagram

%% Models
class MenuItem {
    +CharField name
    +TextField description
    +DecimalField price
    +ImageField image
    +CharField category
    +BooleanField is_vegetarian
    +__str__()
}

class OrderItem {
    +ForeignKey order
    +ForeignKey menu_item
    +PositiveIntegerField quantity
    +__str__()
}

class Order {
    +ForeignKey user
    +DateTimeField order_date
    +DecimalField total_amount
    +ManyToManyField items (through OrderItem)
    +CharField status
    +CharField delivery_address
    +calculate_total()
    +__str__()
}

class User {
    <<Django built-in>>
}

%% Relationships
OrderItem --> MenuItem : menu_item
OrderItem --> Order : order
Order --> User : user
Order --> MenuItem : items (through OrderItem)
