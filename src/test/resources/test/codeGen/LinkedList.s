class Node {
    Int value;
    Node nextNode;
}

impl Node {
    .(){}

    fn Int getValue() {
        ret self.value;
    }

    fn Node getNextNode() {
        ret self.nextNode;
    }

    fn void setValue(Int value) {
        self.value = value;
    }

    fn void setNextNode(Node nextNode) {
        self.nextNode = nextNode;
    }
}

class LinkedList {
    Node head;
}

impl LinkedList {
    .() {
        self.head = nil;
    }

    fn void add(Int value) {
        Node newNode;
        newNode = new Node();
        (newNode.setValue(value));
        (newNode.setNextNode(head));
        head = newNode;
        (IO.out_str("Added: "));
        (IO.out_int(newNode.getValue()));
        (IO.out_str("\n"));

        (IO.out_str("Head: "));
        (IO.out_int(head.getValue()));
        (IO.out_str("\n"));
    }

    fn void print() {
        Node currentNode;
        currentNode = self.head;

        (IO.out_str("- - - - - - - -\n"));
        (IO.out_str("Printing Linked List...\n"));

        (IO.out_str("Head: "));
        (IO.out_int(self.head.getValue()));
        (IO.out_str("\n"));

        (IO.out_str("Current Node: "));
        (IO.out_int(currentNode.getValue()));
        (IO.out_str("\n"));

        (IO.out_bool(currentNode != nil));

        (IO.out_str("Linked List: \n"));

        while (currentNode != nil) {
            (IO.out_str("["));
            (IO.out_int(currentNode.getValue()));
            (IO.out_str("]"));
            currentNode = currentNode.getNextNode();
            if (currentNode != nil) {
                (IO.out_str(" -> "));
            }
        }
    }
}

start {
    LinkedList list;

    list = new LinkedList();

    (list.add(10));
    (list.add(20));
    (list.add(30));

    (list.print());
}