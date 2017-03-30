import matplotlib.pyplot as plt
from matplotlib.mlab import csv2rec
import os.path
from matplotlib.cbook import get_sample_data
from matplotlib.ticker import FormatStrFormatter


for j in range (0, 5):
    if j > 0:
        j += 4
    index = 1
    data1 = []
    data2 = []
    y_axis = []
    x_axis = []
    
    name_array = ['fitness', 'aiType', 'gamesWon', 'averageRedBulletDifference', 'averageBulletDifference', 'redBulletRating', 'bulletPredominanceRating', 'placementRating', 'turnRating']
    
    paramString = name_array[j]
    x_axis_name = 'Generation'
    y_axis_name = paramString
    title = 'Entwicklung von ' + y_axis_name + ' im Laufe der Generationen'
    
    path = 'C:/Users/Juviro/IdeaProjects/Bachelorarbeit/GeneticAlgorithm/gen' + str(index) + '.csv'
    
    while os.path.isfile(path):
        matches_fname = get_sample_data(path)
        matches_data = csv2rec(matches_fname, delimiter=';', names=name_array)
    
        data1.append(max(float(matches_data[paramString][1]), float(matches_data[paramString][2])))
        data2.append(min(float(matches_data[paramString][1]), float(matches_data[paramString][2])))
        y_axis.append(index)
        x_axis.append(0)
        index += 1
        path = 'C:/Users/Juviro/IdeaProjects/Bachelorarbeit/GeneticAlgorithm/gen' + str(index) + '.csv'
    
    
    # You typically want your plot to be ~1.33x wider than tall. This plot
    # is a rare exception because of the number of lines being plotted on it.
    # Common sizes: (10, 7.5) and (12, 9)
    fig, ax = plt.subplots(1, 1, figsize=(12, 9))
    
    # Remove the plot frame lines. They are unnecessary here.
    ax.spines['top'].set_visible(False)
    ax.spines['bottom'].set_visible(False)
    ax.spines['right'].set_visible(False)
    ax.spines['left'].set_visible(False)
    
    # Ensure that the axis ticks only show up on the bottom and left of the plot.
    # Ticks on the right and top of the plot are generally unnecessary.
    ax.get_xaxis().tick_bottom()
    ax.get_yaxis().tick_left()
    
    
    # Make sure your axis ticks are large enough to be easily read.
    # You don't want your viewers squinting to read your plot.
    plt.xticks(range(0, 35, 5), fontsize=14)
    if j != 0:
        plt.yticks(range(-15, 15, 5), fontsize=14)
    else:
        ax.yaxis.set_major_formatter(FormatStrFormatter('%.2f'))
    
    # Provide tick lines across the plot to help your viewers trace along
    # the axis ticks. Make sure that the lines are light and small so they
    # don't obscure the primary data lines.
    for y in range(-150, 200, 10):
        plt.plot(range(0, 45), [y] * len(range(0, 45)), '--',
                 lw=0.5, color='black', alpha=0.3)
    
    # Remove the tick marks; they are unnecessary with the tick lines we just
    # plotted.
    plt.tick_params(axis='both', which='both', bottom='on', top='off',
                    labelbottom='on', left='off', right='off', labelleft='on')
    
    # Now that the plot is prepared, it's time to actually plot the data!
    
    
    
    
    
    
    
    
    
    #print max(data2)
    
    maxValue = max(data1)
    
    #maxValue = max([max(data1), max(data2)])
    
    #print maxValue
    
    
    # Limit the range of the plot to only where the data is.
    # Avoid unnecessary whitespace.
    plt.xlim(-0.5, len(data1) + 2)
    if j != 0:
        plt.ylim(-0.5, 15)
    else:
        plt.ylim(-0.05, 1.1)
    
    plt.xlabel(x_axis_name)
    plt.ylabel(y_axis_name)
    plt.plot(y_axis, data1)
    plt.plot(y_axis, data2)
    #plt.plot(y_axis, x_axis)
    plt.title(title)
    
    
    plt.savefig(title + '.png', bbox_inches='tight')