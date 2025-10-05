# FestUP Android App

This Android project is a **prototype app** for visualizing event areas and crowd density using **Jetpack Compose**. It stubs real event data and displays **heatmaps of crowded areas**, **points of interest**, and **routes to toilets, friends, and emergency exits**.
The main branch is master (we had a problem in changing it to main in the first moment we kept master later)
---

## Features

- **Event list**:  
  Displays events sorted by date with images, title, description, and date.

- **Event map screen**:  
  - Shows the event area as a **polygon**.  
  - Highlights crowded areas using a **heatmap**.  
  - Displays **POIs** (toilets, friends, emergency exits).  
  - **Floating action buttons** to mark queue, friend, or emergency exit locations.

- **Route visualization**:  
  Draws a route from your position to a selected friend, toilet, or emergency exit.

- **Dialogs**:  
  Confirmation dialogs for user actions.

- **UI**:  
  Fully implemented with **Jetpack Compose** for modern declarative UI.

- **Architecture**:  
  MVVM architecture with **Clean Architecture** principles.
