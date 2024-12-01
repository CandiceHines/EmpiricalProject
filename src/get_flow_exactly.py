import networkx as nx

def read_graph_from_file(file_path):
    """
    读取txt文件构造网络流图
    :param file_path: txt文件路径
    :return: NetworkX DiGraph 对象
    """
    G = nx.DiGraph()  # 有向图
    with open(file_path, 'r') as f:
        for line in f:
            u, v, capacity = line.split()
            G.add_edge(u, v, capacity=float(capacity))
    return G

def calculate_max_flow(graph, source, sink):
    """
    计算最大流
    :param graph: NetworkX DiGraph 对象
    :param source: 源点
    :param sink: 汇点
    :return: 最大流值和流分配
    """
    flow_value, flow_dict = nx.maximum_flow(graph, source, sink)
    return flow_value, flow_dict

# 主程序
if __name__ == "__main__":
    # 替换为你的txt文件路径
    file_path = "TestCases/RandomGraph-200-8-1-20.txt"
    
    # 读取图
    G = read_graph_from_file(file_path)
    
    # 设置源点和汇点
    source = 's'
    sink = 't'
    
    # 计算最大流
    max_flow, flow_allocation = calculate_max_flow(G, source, sink)
    
    print(f"最大流值: {max_flow}")
    # print("流分配:")
    # for u, flows in flow_allocation.items():
    #     for v, flow in flows.items():
    #         if flow > 0:
    #             print(f"{u} -> {v}: {flow}")