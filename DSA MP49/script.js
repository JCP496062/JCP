const boardElement = document.getElementById('chess-board');
const newGameButton = document.getElementById('new-game');

function createBoard() {
    for (let row = 0; row < 8; row++) {
        for (let col = 0; col < 8; col++) {
            const square = document.createElement('div');
            square.className = (row + col) % 2 === 0 ? 'square white' : 'square black';
            square.dataset.position = `${row}-${col}`;
            boardElement.appendChild(square);
        }
    }
}

newGameButton.addEventListener('click', () => {
    boardElement.innerHTML = '';
    createBoard();
});

createBoard();