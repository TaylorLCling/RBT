# Taylor Clingenpeel 10/2/2023
from enum import Enum

class Color(Enum):
    RED = 1
    BLACK = 2

class RedBlackTree:

    class Node:
        def __init__(self, data):
            self.data = data
            self.leftChild = None
            self.rightChild = None
            self.grandpa = None
            self.sibling = None
            self.parent = None
            self.uncle = None
            self.color = Color.BLACK

        def leftChild(self):
            return self._leftChild
        
        def leftChild(self, node):
            self._leftChild = node

        def rightChild(self):
            return self._rightChild
        
        def rightChild(self, node):
            self._rightChild = node


    def __init__(self):
        self.root = None

    def treeArrInsert(self, arr):
        for data in arr:
            self.treeInsert(data)

    def treeInsert(self, data):
        if self.root is None:
            self.root = self.Node(data)
            self.root.color = Color.BLACK
        else:
            self._treeInsert(self.root, data)
            self.setRootDown()

    def _treeInsert(self, node, data):
        newNode = self.Node(data)
        newNode.color = Color.RED

        if node.data < data:
            if not node.rightChild or node.rightChild.data == 0:
                node.rightChild = newNode
                self.familySetter(node, newNode)
                self.redBlackSorter(newNode)
            else:
                self._treeInsert(node.rightChild, data)
        elif node.data > data:
            if not node.leftChild or node.leftChild.data == 0:
                node.leftChild = newNode
                self.familySetter(node, newNode)
                self.redBlackSorter(newNode)
            else:
                self._treeInsert(node.leftChild, data)

    def familySetter(self, node, newNode):
        if newNode == self.root:
            return

        self.famSetterInnerFamily(node, newNode)
        if node != self.root:
            self.famSetterExtended(node, newNode)
        if node.parent:
            self.familySetter(node.parent, newNode.parent)

    def famSetterInnerFamily(self, node, newNode):
        newNode.parent = node
        if node.leftChild and node.rightChild:
            if node.leftChild == newNode:
                newNode.sibling = node.rightChild
                node.rightChild.sibling = newNode
            elif node.rightChild == newNode:
                newNode.sibling = node.leftChild
                node.leftChild.sibling = newNode

    def famSetterExtended(self, node, newNode):
        newNode.grandpa = node.parent
        if node.parent.leftChild and node.parent.rightChild:
            if node == node.parent.leftChild:
                newNode.uncle = node.parent.rightChild
            else:
                newNode.uncle = node.parent.leftChild

    def setRootDown(self):
        self._setRootDown(self.root, self.root.leftChild)
        self._setRootDown(self.root, self.root.rightChild)

    def _setRootDown(self, node, child):
        if not child:
            return
        self.familySetter(node, child)
        self._setRootDown(child, child.leftChild)
        self._setRootDown(child, child.rightChild)

    def redBlackSorter(self, newNode):
        if newNode.parent.color == Color.BLACK:
            return
        elif newNode.parent.color == Color.RED:
            self.case(newNode)  # The case method needs to be defined
        else:
            print("Unexpected case in RedBlackSorter")
        self.familySetter(newNode.parent, newNode)
        self.root.color = Color.BLACK

    def createFakeUncle(self, newNode):
        fakeUncle = self.Node(0)
        newNode.parent.sibling = fakeUncle
        fakeUncle.sibling = newNode.parent
        newNode.uncle = fakeUncle

        if newNode.parent == newNode.grandpa.leftChild:
            newNode.grandpa.rightChild = fakeUncle
        else:
            newNode.grandpa.leftChild = fakeUncle

        fakeUncle.parent = newNode.grandpa
        if newNode.grandpa != self.root:
            fakeUncle.grandpa = newNode.grandpa.parent
        fakeUncle.color = Color.BLACK

        UL, UR = self.Node(0), self.Node(0)
        fakeUncle.leftChild, fakeUncle.rightChild = UL, UR
        UL.parent, UR.parent = fakeUncle, fakeUncle
        UL.uncle, UR.uncle = fakeUncle.sibling, fakeUncle.sibling
        UL.grandpa, UR.grandpa = fakeUncle.parent, fakeUncle.parent

        return fakeUncle

    def createFakeGrandpa(self, newNode):
        fakeGrandpa = self.Node(0)
        newNode.grandpa.parent = fakeGrandpa
        newNode.uncle.grandpa = fakeGrandpa
        newNode.parent.grandpa = fakeGrandpa
        newNode.parent.uncle = None
        newNode.uncle.uncle = None
        fakeGrandpa.leftChild = newNode.grandpa
        fakeGrandpa.color = Color.BLACK

        return fakeGrandpa

    def case(self, newNode):
        if newNode in [self.root, newNode.parent, newNode.grandpa]:
            return
        if newNode.grandpa == self.root:
            if newNode.uncle is None or (newNode.uncle.data == 0 and newNode.uncle.color == Color.BLACK):
                fakeUncle = self.createFakeUncle(newNode)
                fakeGGrandpa = self.createFakeGrandpa(newNode)
                self.casePart2(newNode)
                # Resetting fake nodes
                fakeUncle.leftChild, fakeUncle.rightChild = None, None
                fakeUncle, fakeGGrandpa = None, None
            else:
                fakeGGrandpa = self.createFakeGrandpa(newNode)
                self.casePart2(newNode)
                fakeGGrandpa = None
        elif newNode.uncle is None or (newNode.uncle.data == 0 and newNode.uncle.color == Color.BLACK):
            fakeUncle = self.createFakeUncle(newNode)
            self.casePart2(newNode)
            fakeUncle.leftChild, fakeUncle.rightChild = None, None
            fakeUncle = None
        elif newNode.grandpa and newNode.uncle:
            self.casePart2(newNode)

    def casePart2(self, newNode):
        if newNode in [self.root, newNode.parent, newNode.grandpa]:
            return

        if newNode.uncle.color == Color.RED:
            self.case1(newNode)
            if newNode not in [self.root, newNode.parent, newNode.grandpa]:
                self.redBlackSorter(newNode.grandpa)
        elif newNode.uncle.color == Color.BLACK:
            self.case2(newNode)
            if newNode not in [self.root, newNode.parent, newNode.grandpa]:
                self.redBlackSorter(newNode.grandpa)
        else:
            print("Unexpected case in casePart2 method. Uncle is neither black nor red.")

    def case1(self, newNode):
        if any([newNode.uncle is None, newNode.grandpa is None or newNode.grandpa.data == 0, newNode.parent is None]):
            print("Null family was given to Case 1")
            return
        newNode.parent.color, newNode.uncle.color, newNode.grandpa.color = Color.BLACK, Color.BLACK, Color.RED
        self.root.color = Color.BLACK

    def case2(self, newNode):
        T1, T2, T3, T4, T5 = self.Node(0), self.Node(0), self.Node(0), self.Node(0), self.Node(0)

        if newNode in [self.root, newNode.leftChild, newNode.rightChild]:
            return

        if newNode.parent.data < newNode.grandpa.data:
            if newNode.data < newNode.parent.data:
                if newNode.parent.rightChild is None:
                    T3.parent, T3.grandpa, T3.sibling, T3.uncle = newNode.parent, newNode.grandpa, newNode, newNode.uncle
                    newNode.parent.rightChild, newNode.sibling = T3, T3
                    self.rightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                    T3 = None
                else:
                    self.rightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
            elif newNode.data > newNode.parent.data:
                if not newNode.parent.leftChild or not newNode.leftChild or not newNode.rightChild:
                    if not newNode.parent.leftChild and not newNode.leftChild and not newNode.rightChild:
                        T1.parent, T2.parent, T3.parent = newNode.parent, newNode, newNode
                        newNode.parent.leftChild, newNode.leftChild, newNode.rightChild = T1, T2, T3
                        T1.uncle, T2.uncle, T3.uncle = newNode.uncle, T1, T1
                        T2.sibling, T3.sibling, T1.sibling, newNode.sibling = T3, T2, newNode, T1
                        tempNode = newNode
                        newNode = self.smallLeftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                        self.rightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                        T3, T4, T5 = None, None, None
                        newNode = tempNode
                    else:
                        if not newNode.parent.leftChild:
                            T1.parent = newNode.parent
                            newNode.parent.leftChild = T1
                            T1.uncle, T1.sibling, newNode.sibling = newNode.uncle, newNode, T1
                        if not newNode.leftChild:
                            T2.parent, newNode.leftChild = newNode, T2
                            T2.uncle, T2.sibling = T1, T3
                        if not newNode.rightChild:
                            T3.parent, newNode.rightChild = newNode, T3
                            T3.uncle, T3.sibling = T1, T3

                        tempNode = newNode
                        newNode = self.smallLeftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                        self.rightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                        T1, T2, T3 = None, None, None
                        newNode = tempNode
                        return
                else:
                    tempNode = newNode
                    newNode = self.smallLeftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                    self.rightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                    newNode = tempNode
                    return


        elif newNode.parent.data > newNode.grandpa.data:
            if newNode.parent.data < newNode.data:
                if not newNode.parent.leftChild or newNode.parent.leftChild.data == 0:
                    T3.parent, T3.grandpa, T3.sibling, T3.uncle = newNode.parent, newNode.grandpa, newNode, newNode.uncle
                    newNode.parent.leftChild, newNode.sibling = T3, T3
                    self.leftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                    T3 = None
                else:
                    self.leftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
            elif newNode.parent.data > newNode.data:
                if not newNode.leftChild or not newNode.rightChild or not newNode.parent.rightChild:
                    if not newNode.leftChild and not newNode.rightChild and not newNode.parent.rightChild:
                        newNode.leftChild, newNode.rightChild, newNode.parent.rightChild = T3, T4, T5
                        T3.parent, T3.uncle, T4.uncle, T4.parent, T5.parent = newNode, T5, T5, newNode, newNode.parent
                        T3.sibling, T4.sibling, T5.sibling = T4, T3, newNode
                        tempNode = newNode
                        newNode = self.smallRightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                        self.leftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                        T3, T4, T5 = None, None, None
                        newNode = tempNode
                    else:
                        if not newNode.parent.rightChild:
                            newNode.parent.rightChild, T5.parent, T5.sibling, T5.uncle = T5, newNode.parent, newNode, newNode.uncle
                        if not newNode.rightChild:
                            newNode.rightChild, T4.uncle, T4.parent, T4.sibling = T4, T5, newNode, T3
                        if not newNode.leftChild:
                            newNode.leftChild, T3.parent, T3.uncle, T3.sibling = T3, newNode, T5, T5
                        tempNode = newNode
                        newNode = self.smallRightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                        self.leftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                        T3, T4, T5 = None, None, None
                        newNode = tempNode
                else:
                    tempNode = newNode
                    newNode = self.smallRightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                    self.leftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle)
                    newNode = tempNode

    def rightRotate(self, node, parent, grandpa, uncle):
        grandpa.leftChild = parent.rightChild
        parent.rightChild = grandpa
        parent.parent = grandpa.parent
        grandpa.parent.leftChild = parent
        grandpa.parent = parent
        node.grandpa = node.parent.parent
        grandpa.leftChild.parent = grandpa
        grandpa.leftChild.grandpa = parent
        node.sibling = grandpa
        parent.sibling = grandpa.sibling
        grandpa.sibling = node
        uncle.sibling = grandpa.leftChild
        grandpa.leftChild.sibling = uncle
        node.uncle = parent.sibling
        grandpa.uncle = parent.sibling
        grandpa.color = Color.RED
        parent.color = Color.BLACK
        parent.grandpa = parent.parent.parent
        grandpa.grandpa = parent.parent
        uncle.grandpa = parent
        uncle.uncle = node
        if node.leftChild:
            node.leftChild.uncle = grandpa
        if node.rightChild:
            node.rightChild.uncle = grandpa
        grandpa.leftChild.uncle = node
        uncle.leftChild.uncle = grandpa.leftChild
        uncle.rightChild.uncle = grandpa.leftChild
        if parent.parent.leftChild == grandpa:
            parent.parent.leftChild = parent
        elif parent.parent.rightChild == grandpa:
            parent.parent.rightChild = parent
        if grandpa == self.root:
            self.root = parent
    
    def smallRightRotate(self, node, parent, grandpa, uncle):
        grandpa.rightChild, node.parent = node, grandpa
        node.grandpa, node.uncle = grandpa.parent, grandpa.sibling
        x = node.rightChild
        node.rightChild, parent.parent = parent, node
        parent.grandpa, parent.uncle = grandpa, uncle
        parent.leftChild = x
        parent.sibling = node.rightChild
        grandpa.rightChild, node.sibling = node, uncle
        if uncle:
            uncle.leftChild.uncle = node
            uncle.rightChild.uncle = node
        node.rightChild.grandpa, node.rightChild.uncle = grandpa, uncle
        node.rightChild.sibling = parent
        if parent.leftChild:
            parent.leftChild.parent, parent.leftChild.grandpa, parent.leftChild.uncle = parent, node, node.leftChild
            parent.leftChild.sibling = parent.rightChild
        if parent.rightChild:
            parent.rightChild.grandpa, parent.rightChild.uncle = node, node.leftChild
            parent.rightChild.sibling = parent.leftChild
        return parent

    def leftRotate(self, node, parent, grandpa, uncle):
        grandpa.rightChild = parent.leftChild
        if grandpa.rightChild:
            grandpa.rightChild.parent = grandpa
        parent.leftChild = grandpa
        parent.parent = grandpa.parent
        grandpa.parent = parent
        node.grandpa = node.parent.parent
        grandpa.rightChild.grandpa = parent
        grandpa.rightChild.uncle = node
        if node.leftChild:
            node.leftChild.uncle = grandpa
        if node.rightChild:
            node.rightChild.uncle = grandpa
        node.sibling = grandpa
        parent.sibling = grandpa.sibling
        if grandpa.sibling:
            grandpa.sibling.sibling = parent
        grandpa.sibling = node
        uncle.sibling = grandpa.rightChild
        node.uncle = parent.sibling
        grandpa.uncle = parent.sibling
        parent.color = Color.BLACK
        grandpa.color = Color.RED
        grandpa.rightChild.sibling = uncle
        uncle.grandpa = parent
        uncle.uncle = node
        grandpa.grandpa = parent.parent
        parent.grandpa = parent.parent.parent
        parent.uncle = parent.parent.sibling
        if parent.parent.leftChild == grandpa:
            parent.parent.leftChild = parent
        elif parent.parent.rightChild == grandpa:
            parent.parent.rightChild = parent
        if grandpa == self.root:
            self.root = parent

    def smallLeftRotate(self, node, parent, grandpa, uncle):
        node.parent, x, parent.rightChild = grandpa, node.leftChild, x
        node.leftChild, parent.parent = parent, node
        node.rightChild.grandpa, node.rightChild.sibling, node.rightChild.uncle = grandpa, parent, uncle
        parent.leftChild.parent, parent.leftChild.uncle, parent.leftChild.sibling = parent, node.rightChild, parent.rightChild
        parent.rightChild.parent, parent.rightChild.grandpa = parent, node
        grandpa.leftChild, node.sibling, node.uncle = node, uncle, grandpa.sibling
        if uncle:
            uncle.leftChild.uncle, uncle.rightChild.uncle = node, node
        return parent











    def printPreOrder(self):
        print("Printing in Pre-Order")
        self._printPreOrder(self.root)

    def _printPreOrder(self, node):
        if not node or node.data == 0:
            return
        print(node.data, end=" ")
        print("Black" if node.color == Color.BLACK else "Red")
        if node.leftChild:
            self._printPreOrder(node.leftChild)
        if node.rightChild:
            self._printPreOrder(node.rightChild)

    def printInOrder(self):
        print("Printing In-Order")
        self._printInOrder(self.root)

    def _printInOrder(self, node):
        if not node or node.data == 0:
            return
        self._printInOrder(node.leftChild)
        print(node.data, end=" ")
        print("Black" if node.color == Color.BLACK else "Red")
        self._printInOrder(node.rightChild)

    def printPostOrder(self):
        print("Printing in Post-Order")
        self._printPostOrder(self.root)

    def _printPostOrder(self, node):
        if not node or (node.data == 0 and node.color == Color.BLACK):
            return
        if node.leftChild:
            self._printPostOrder(node.leftChild)
        if node.rightChild:
            self._printPostOrder(node.rightChild)
        print(node.data, end=" ")
        print("Black" if node.color == Color.BLACK else "Red")

    def treeHeight(self):
        self.height = 0
        if not self.root:
            return 0
        if not self.root.leftChild and not self.root.rightChild:
            return 1
        self._treeHeight(self.root)
        return self.height

    def _treeHeight(self, node):
        if node.data == 0 and node.color == Color.BLACK:
            return
        if node == self.root:
            self.height = 1
        if node.leftChild and node.leftChild.data != 0:
            self._treeHeight(node.leftChild)
            self.height += 1
        if node.rightChild and not node.leftChild:
            self._treeHeight(node.rightChild)
            self.height += 1
        if node.leftChild and node.rightChild:
            self._treeHeight(node.rightChild)

    def printLevelOrder(self):
        print("Printing in Level-Order")
        for d in range(1, self.treeHeight() + 1):
            if d > 1:
                print()
            print(d, end=" ")
            self._printGivenLevel(d, self.root)

    def _printGivenLevel(self, level, node):
        if not node:
            return
        if level == 1:
            if not node or (node.data == 0 and node.color == Color.BLACK):
                return
            print(f"({node.data}", end=" ")
            print("Black)" if node.color == Color.BLACK else "Red)", end=" ")
        else:
            self._printGivenLevel(level - 1, node.leftChild)
            self._printGivenLevel(level - 1, node.rightChild)

    def FamilyPicture(self):
        for d in range(1, self.treeHeight() + 1):
            self._FamilyPicture(d, self.root)

    def _FamilyPicture(self, level, node):
        if not node:
            return
        if level == 1:
            if not node or (node.data == 0 and node.color == Color.BLACK):
                return
            self.printNodeInfo(node)
        else:
            self._FamilyPicture(level - 1, node.leftChild)
            self._FamilyPicture(level - 1, node.rightChild)

    def printNodeInfo(self, node):
        print("\n\n")
        if node == self.root:
            print("Root")
        print(f"Data = {node.data}", end=" | ")
        print("Color = Black" if node.color == Color.BLACK else "Color = Red", end=" | ")
        print(f"Level = {self.GetDepth(node)}")
        print(f"Parent {node.parent.data}" if node.parent and node.parent.data != 0 else "No Parent")
        print(f"Grandpa {node.grandpa.data}" if node.grandpa and node.grandpa.data != 0 else "No Grandpa")
        print(f"Uncle {node.uncle.data}" if node.uncle and node.uncle.data != 0 else "No Uncle")
        print(f"Sibling {node.sibling.data}" if node.sibling and node.sibling.data != 0 else "No Sibling")
        if node.leftChild and node.rightChild:
            print(f"Left = {node.leftChild.data} | Right = {node.rightChild.data}")
        elif not node.leftChild and node.rightChild:
            print(f"Left = None | Right = {node.rightChild.data}")
        elif node.leftChild and not node.rightChild:
            print(f"Left = {node.leftChild.data} | Right = None")
        else:
            print("No Children")

    depthheight = 0

    def GetDepth(self, treasure):
        self.depthheight = 0
        if not self.root:
            return 0
        if not self.root.leftChild and not self.root.rightChild:
            return 1
        self._GetDepth(treasure, self.root)
        return self.depthheight

    def _GetDepth(self, treasure, node):
        if node.data == 0 and node.color == Color.BLACK:
            return
        if node == self.root:
            self.depthheight = 1
        if treasure == node:
            return
        if treasure.data < node.data:
            self.depthheight += 1
            self._GetDepth(treasure, node.leftChild)
        else:
            self.depthheight += 1
            self._GetDepth(treasure, node.rightChild)            