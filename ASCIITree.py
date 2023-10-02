from RBTree import RedBlackTree

class ASCII_Tree_Visualizer:
    @staticmethod
    def visualize_tree(node):
        """Return a list of strings representing the tree."""
        lines, _, _, _ = ASCII_Tree_Visualizer._visualize_tree_helper(node)
        return lines

    @staticmethod
    def _visualize_tree_helper(node):
        """Returns list of strings, width, height, and horizontal coordinate of root."""
        if node is None or node.data == 0:
            return [], 0, 0, 0

        label = f"{node.data}({node.color.name[0]})"
        left_lines, left_pos, left_width, left_coord = ASCII_Tree_Visualizer._visualize_tree_helper(node.leftChild)
        right_lines, right_pos, right_width, right_coord = ASCII_Tree_Visualizer._visualize_tree_helper(node.rightChild)

        middle = max(right_pos, left_width - left_pos + 1) + len(label)
        pos = left_pos + middle // 2
        width = left_pos + middle + right_width - right_pos

        while len(left_lines) < len(right_lines):
            left_lines.append(' ' * left_width)
        while len(right_lines) < len(left_lines):
            right_lines.append(' ' * right_width)

        if (middle - len(label)) % 2 == 1 and node.parent is not None and \
           node is node.parent.leftChild and len(label) < middle:
            label += '.'
        label = label.center(middle, '.')

        if label[0] == '.':
            label = ' ' + label[1:]
        if label[-1] == '.':
            label = label[:-1] + ' '

        lines = [' ' * left_pos + label + ' ' * (right_width - right_pos),
                 ' ' * left_pos + '/' + ' ' * (middle - 2) +
                 '\\' + ' ' * (right_width - right_pos)]

        for left_line, right_line in zip(left_lines, right_lines):
            lines.append(left_line + ' ' * (width - left_width - right_width) + right_line)

        return lines, pos, width, pos

def print_ascii_tree(tree):
    lines = ASCII_Tree_Visualizer.visualize_tree(tree.root)
    for line in lines:
        print(line)
