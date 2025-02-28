
# BasketSplitter

BasketSplitter class represents a utility for splitting items into delivery  
methods based on a configuration file. It reads a <b>JSON</b> configuration file and  
performs the splitting of items into delivery methods accordingly.

> The library is written in Java 17 using the Maven version control system.
Also javadoc documentation was written for this package including private methods for developers.
Dependencies and configurations for generating the "fat-jar" build with all dependencies were written in Maven configurations


***

# Problem

At our online supermarket, we want to expand the range of products
that we sell with non-food items, such as electronics or items for the home. 

These are characterized by the fact that they often cannot be delivered by our standard
delivery truck because they are often simply too large.  
The second reason is that we will also offer items that are at external
suppliers and we cannot deliver them with our fleet.  

In both such cases, the purchases customer need to be sent by specialized courier.  
On the other hand, it is obvious that couriers cannot deliver all the items we sell, especially food products.  
Due to the fact that not all products can be sent by all means of
delivery, we need to divide the items in the customer's online shopping cart into delivery groups.  

The task is to optimally divide the items in the shopping cart so as to minimize the
the number of required deliveries. After determining the minimum number of delivery groups, make sure
make sure to create the largest possible delivery groups.  

We have observed that customers often decide only to purchase items from the largest
delivery group, leaving the rest in the shopping cart.



***

# Solution

1. Constructs a BasketSplitter object with the absolute path to the configuration file.  
Uses the private method <i>setConfig()</i> to create a configuration map and stores it inside private parameter.

2.  Create an object that stores a list of all keys, sorted by the number of values of each key

    > Since `HashMap` does not guarantee the order of the elements, we will need this step in the future to compare the sorted elements

3. Using the filterDeliveryMap method we start comparing the keys with the smallest number of values with the keys with the largest number of values and look for matches. 
    1. If found, we remove the value from the smaller key. 
    2. If not - search in the second key by the number of values and so on.
    3. If the values of the key remain empty - delete it.

This way we exclude the possibility that the least popular shipping method will have values that can be carried by a more populated shipping method.  
<b>Minimize</b> the total number of delivery methods and <b>maximize</b> the number of items that can be carried by one delivery method.

