
from RBTree import RedBlackTree
from ASCIITree import ASCII_Tree_Visualizer, print_ascii_tree

class TreeCar:
    def __init__(self):
        self.Tree = RedBlackTree()
        self.ASCII = ASCII_Tree_Visualizer()

    def start(self):
        while True:
            print("\nRed-Black Tree Implementation")
            print("1. Insert")
            print("2. Bulk Insert")
            print("3. Display (Level-Order)")
            print("4. Tree Height")
            print("5. Display Detailed Info (Family Picture)")
            print("6. ASCII Visualization")
            print("10. Exit")
            
            choice = int(input("Enter your choice: "))
            
            if choice == 1:
                data = int(input("Enter Data to Insert: "))
                self.Tree.treeInsert(data)
            elif choice == 2:
                nodes_str = input("Enter nodes (comma-separated): ")
                nodes = [int(node.strip()) for node in nodes_str.split(',')]
                self.Tree.treeArrInsert(nodes)
            elif choice == 3:
                self.Tree.printLevelOrder()
            elif choice == 4:
                print(f"Tree Height: {self.Tree.treeHeight()}")
            elif choice == 5:
                self.Tree.FamilyPicture()
            elif choice == 6:
                print_ascii_tree(self.Tree)
            elif choice == 10:
                break
            else:
                print("Invalid choice! Please choose a valid option.")

if __name__ == "__main__":
    treeCar = TreeCar()
    treeCar.start()