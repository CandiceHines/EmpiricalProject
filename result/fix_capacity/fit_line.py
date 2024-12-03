import numpy as np
import matplotlib.pyplot as plt

# Assume you have the following data
# x = np.array([1, 10, 100, 500, 1000])
# y = np.array([6.4, 23.4, 129.6, 1099.2, 4347.8])
x = np.array([1, 10, 100, 500, 1000])
y = np.array([9, 23.4, 69.6, 210.6, 308.8])
# Use polyfit to fit a line without an intercept
slope, intercept = np.polyfit(x, y, 1)

# Calculate predicted values based on the fitted result
y_pred = slope * x

# Calculate R^2 coefficient of determination
ss_total = np.sum((y - np.mean(y)) ** 2)  # Total sum of squares
ss_residual = np.sum((y - y_pred) ** 2)   # Residual sum of squares
r_squared = 1 - (ss_residual / ss_total)

# Output the fitting results
print(f"Slope of the fitted line: {slope}")
print(f"Intercept of the fitted line: {intercept}")
print(f"Coefficient of determination R^2: {r_squared}")

# Plot the original data and the fitted line
plt.scatter(x, y, label='Original Data')
plt.plot(x, y_pred, color='red', label='Fitted Line (No Intercept)')
plt.xlabel('X')
plt.ylabel('Y')
plt.legend()
plt.show()