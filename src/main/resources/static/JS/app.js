// BUSCADOR

document.addEventListener('DOMContentLoaded', function () {
    cargarPeliculas(); 
    const input = document.getElementById('valorBusqueda');
    const criterio = document.getElementById('criterioBusqueda');

    if (input) {
        input.addEventListener('input', function() {
            const valor = input.value.trim();
            const crit = criterio.value;

            if (valor === '') {
                cargarPeliculas(); 
                return;
            }

            fetch('/peliculas/buscar?criterio=' + crit + '&valor=' + encodeURIComponent(valor))
                .then(res => res.json())
                .then(peliculas => {
                    renderizarCards(peliculas);
                });
        });
    }
});

function limpiarBusqueda() {
    document.getElementById('valorBusqueda').value = '';
    cargarPeliculas();
}

function cargarPeliculas() {
    fetch('/peliculas')
        .then(res => res.json())
        .then(peliculas => {
            renderizarCards(peliculas);
        });
}

function renderizarCards(peliculas) {
    const grilla = document.getElementById('grillaPeliculas');

    if (peliculas.length === 0) {
        grilla.innerHTML = `
            <div class="col-12 text-center py-5">
                <p class="text-muted">No se encontraron resultados</p>
            </div>`;
        return;
    }

    grilla.innerHTML = peliculas.map(p => `
        <div class="col">
            <div class="card shadow-sm">
                <div class="card-img-container">
                    <img src="/imagen?url=${encodeURIComponent(p.urlPoster)}" 
                         class="card-img-blur" alt="">
                    <img src="/imagen?url=${encodeURIComponent(p.urlPoster)}" 
                         class="card-img-front" alt="">
                </div>
                <div class="card-body">
                    <h5>${p.titulo}</h5>
                    <p class="card-text">${p.sinopsis || ''}</p>
                    <div class="d-flex justify-content-between align-items-center">
                        <div class="btn-group">
                            <button class='btn btn-sm btn-outline-secondary' onclick='verDetalle(${p.id})'>Ver</button>
                            <button class='btn btn-sm btn-outline-secondary' onclick='editarPelicula(${p.id})'>Editar</button>
                            <button class='btn btn-sm btn-outline-danger' data-id='${p.id}' onclick='eliminarPelicula(this)'>Eliminar</button>
                        </div>
                        <small class="text-muted">⭐ ${p.calificacionPromedio}</small>
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}



//AÑADIR AL CATALOGO

function buscarEnTmdb() {
    const query = document.getElementById('busquedaTmdb').value.trim();

    if (query === '') return;

    const resultados = document.getElementById('resultadosTmdb');
    const mensaje = document.getElementById('mensajeTmdb');

    resultados.innerHTML = '<p class="text-muted">Buscando...</p>';
    mensaje.classList.add('d-none');

    fetch('/tmdb/buscar?query=' + encodeURIComponent(query))
        .then(res => res.json())
        .then(data => {
            if (!data.results || data.results.length === 0) {
                resultados.innerHTML = '';
                mensaje.className = 'alert alert-warning';
                mensaje.textContent = 'No se encontraron resultados en TMDB';
                mensaje.classList.remove('d-none');
                return;
            }
resultados.innerHTML = data.results.slice(0, 9).map(p => `
    <div class="col">
        <div class="card h-100 shadow-sm">
            <img src="${p.poster_path ? '/imagen?url=' + encodeURIComponent('https://image.tmdb.org/t/p/w500' + p.poster_path) : ''}" 
                 class="card-img-top" style="height:200px; object-fit:cover" alt="">
            <div class="card-body p-2">
                <small><strong>${p.title}</strong></small><br>
                <small class="text-muted">${p.release_date ? p.release_date.substring(0,4) : ''}</small>
            </div>
            <div class="card-footer p-2">
                <button class="btn btn-sm btn-success w-100"
                        data-tmdb-id="${p.id}"
                        onclick="agregarDesdeTmdb(this)">
                    + Agregar
                </button>
            </div>
        </div>
    </div>
`).join('');
        });
}

function escapar(texto) {
    if (!texto) return '';
    return texto.replace(/'/g, "\\'").replace(/"/g, '\\"').replace(/\n/g, ' ');
}



// Buscar también al presionar Enter
document.addEventListener('DOMContentLoaded', function() {
    const inputTmdb = document.getElementById('busquedaTmdb');
    const btnBuscar = document.getElementById('btnBuscarTmdb');

    if (btnBuscar) {
        btnBuscar.addEventListener('click', buscarEnTmdb);
    }

    if (inputTmdb) {
        inputTmdb.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') buscarEnTmdb();
        });
    }
});

// PARA EL TMDB NOS ENTREGE GENERO Y DIRECTOR (que vaina que de los datos en otro formato)

function agregarDesdeTmdb(btn) {
    const tmdbId = btn.getAttribute('data-tmdb-id');
    btn.textContent = 'Agregando...';
    btn.disabled = true;

    fetch('/tmdb/detalles/' + tmdbId)
        .then(res => res.json())
        .then(detalles => {
            const pelicula = {
                titulo: detalles.title || detalles.original_title,
                anio: detalles.release_date ? parseInt(detalles.release_date.substring(0, 4)) : 0,
                sinopsis: detalles.overview || '',
                urlPoster: detalles.poster_path ? 'https://image.tmdb.org/t/p/w500' + detalles.poster_path : '',
                genero: detalles.generoNombre || '',
                director: detalles.directorNombre || '',
                calificacionPromedio: 0.0
            };

            fetch('/peliculas', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(pelicula)
            })
            .then(res => res.json())
            .then(guardada => {
                const mensaje = document.getElementById('mensajeTmdb');
                mensaje.className = 'alert alert-success';
                mensaje.textContent = '✅ "' + guardada.titulo + '" agregada al catálogo';
                mensaje.classList.remove('d-none');

                setTimeout(() => {
                    bootstrap.Modal.getInstance(document.getElementById('modalTmdb')).hide();
                    cargarPeliculas();
                }, 1500);
            });
        });
}
// ===== VARIABLES GLOBALES =====
let peliculaActualId = null;

// ===== MODAL VER DETALLE =====
function verDetalle(id) {
    peliculaActualId = id;

    fetch('/peliculas/' + id)
        .then(res => res.json())
        .then(pelicula => {
            // Llenar datos
            document.getElementById('detalleTitulo').textContent = pelicula.titulo;
            document.getElementById('detalleDirector').textContent = pelicula.director || 'No disponible';
            document.getElementById('detalleGenero').textContent = pelicula.genero || 'No disponible';
            document.getElementById('detalleAnio').textContent = pelicula.anio;
            document.getElementById('detallePromedio').textContent = pelicula.calificacionPromedio;
            document.getElementById('detalleSinopsis').textContent = pelicula.sinopsis || '';

            // Póster
            const url = '/imagen?url=' + encodeURIComponent(pelicula.urlPoster);
            document.getElementById('detallePoster').src = url;
            document.getElementById('detallePosterFront').src = url;

            // Cargar reseñas
            cargarResenas(id);

            // Mostrar sección de reseñas, ocultar formulario
            document.getElementById('seccionResenas').classList.remove('d-none');
            document.getElementById('formularioResena').classList.add('d-none');

            // Abrir modal
            new bootstrap.Modal(document.getElementById('modalDetalle')).show();
        });
}

function cargarResenas(peliculaId) {
    fetch('/peliculas/' + peliculaId + '/resenas')
        .then(res => res.json())
        .then(resenas => {
            const lista = document.getElementById('listaResenas');

            if (resenas.length === 0) {
                lista.innerHTML = '<p class="text-muted">No hay reseñas todavía. ¡Sé el primero!</p>';
                return;
            }

            lista.innerHTML = resenas.map(r => `
                <div class="border rounded p-2 mb-2">
                    <div class="d-flex justify-content-between">
                        <strong>${r.nombreAutor}</strong>
                        <span>⭐ ${r.calificacion}/5</span>
                    </div>
                    <p class="mb-1 mt-1">${r.comentario}</p>
                    <small class="text-muted">${r.fecha}</small>
                </div>
            `).join('');
        });
}

function mostrarFormularioResena() {
    document.getElementById('seccionResenas').classList.add('d-none');
    document.getElementById('formularioResena').classList.remove('d-none');

    // Poner fecha de hoy por defecto
    const hoy = new Date().toISOString().split('T')[0];
    document.getElementById('resFecha').value = hoy;
    document.getElementById('resFecha').max = hoy;

    // Limpiar campos
    document.getElementById('resNombre').value = '';
    document.getElementById('resComentario').value = '';
    document.getElementById('resCalificacion').value = '';
    document.getElementById('mensajeResena').classList.add('d-none');
}

function cancelarResena() {
    document.getElementById('formularioResena').classList.add('d-none');
    document.getElementById('seccionResenas').classList.remove('d-none');
}

function guardarResena() {
    const nombre = document.getElementById('resNombre').value.trim();
    const comentario = document.getElementById('resComentario').value.trim();
    const calificacion = parseFloat(document.getElementById('resCalificacion').value);
    const fecha = document.getElementById('resFecha').value;

    // Validaciones
    let valido = true;

    if (!nombre) {
        document.getElementById('errorNombre').classList.remove('d-none');
        valido = false;
    } else {
        document.getElementById('errorNombre').classList.add('d-none');
    }

    if (!comentario) {
        document.getElementById('errorComentario').classList.remove('d-none');
        valido = false;
    } else {
        document.getElementById('errorComentario').classList.add('d-none');
    }

    if (!calificacion || calificacion < 1 || calificacion > 5) {
        document.getElementById('errorCalificacion').classList.remove('d-none');
        valido = false;
    } else {
        document.getElementById('errorCalificacion').classList.add('d-none');
    }

    if (!fecha) {
        document.getElementById('errorFecha').classList.remove('d-none');
        valido = false;
    } else {
        document.getElementById('errorFecha').classList.add('d-none');
    }

    if (!valido) return;

    const resena = {
        nombreAutor: nombre,
        comentario: comentario,
        calificacion: calificacion,
        fecha: fecha
    };

    fetch('/peliculas/' + peliculaActualId + '/resenas', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(resena)
    })
    .then(res => res.json())
    .then(() => {
        const mensaje = document.getElementById('mensajeResena');
        mensaje.className = 'alert alert-success';
        mensaje.textContent = '✅ Reseña guardada exitosamente';
        mensaje.classList.remove('d-none');

        setTimeout(() => {
            cancelarResena();
            cargarResenas(peliculaActualId);
            cargarPeliculas(); // actualiza el promedio en las cards
        }, 1000);
    });
}

// ===== MODAL EDITAR =====
function editarPelicula(id) {
    fetch('/peliculas/' + id)
        .then(res => res.json())
        .then(pelicula => {
            document.getElementById('editId').value = pelicula.id;
            document.getElementById('editTitulo').value = pelicula.titulo;
            document.getElementById('editGenero').value = pelicula.genero || '';
            document.getElementById('editAnio').value = pelicula.anio;
            document.getElementById('editDirector').value = pelicula.director || '';
            document.getElementById('editUrlPoster').value = pelicula.urlPoster || '';
            document.getElementById('editSinopsis').value = pelicula.sinopsis || '';
            document.getElementById('mensajeEditar').classList.add('d-none');
            document.getElementById('editCalificacion').value = pelicula.calificacionPromedio || 0.0;

            new bootstrap.Modal(document.getElementById('modalEditar')).show();
        });
}

function guardarEdicion() {
    const id = document.getElementById('editId').value;
    const titulo = document.getElementById('editTitulo').value.trim();

    // Validaciones
    let valido = true;

    if (!titulo) {
        document.getElementById('errorEditTitulo').classList.remove('d-none');
        valido = false;
    } else {
        document.getElementById('errorEditTitulo').classList.add('d-none');
    }

    if (!valido) return;

    const anioVal = document.getElementById('editAnio').value;
    const anio = anioVal && anioVal !== '' ? parseInt(anioVal) : new Date().getFullYear();

    const pelicula = {
        titulo: titulo,
        genero: document.getElementById('editGenero').value.trim(),
        anio: anio,
        director: document.getElementById('editDirector').value.trim(),
        sinopsis: document.getElementById('editSinopsis').value.trim(),
        urlPoster: document.getElementById('editUrlPoster').value,
        calificacionPromedio: parseFloat(document.getElementById('editCalificacion').value) || 0.0
    };

    fetch('/peliculas/' + id, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(pelicula)
    })
    .then(res => res.json())
    .then(() => {
        const mensaje = document.getElementById('mensajeEditar');
        mensaje.className = 'alert alert-success';
        mensaje.textContent = '✅ Película actualizada exitosamente';
        mensaje.classList.remove('d-none');

        setTimeout(() => {
            bootstrap.Modal.getInstance(document.getElementById('modalEditar')).hide();
            cargarPeliculas();
        }, 1000);
    });
}

// ===== MODAL ELIMINAR =====
function eliminarPelicula(btn) {
    peliculaActualId = btn.getAttribute('data-id');
    const titulo = btn.getAttribute('data-titulo');
    document.getElementById('eliminarNombre').textContent = titulo;
    new bootstrap.Modal(document.getElementById('modalEliminar')).show();
}

function confirmarEliminar() {
    fetch('/peliculas/' + peliculaActualId, {
        method: 'DELETE'
    })
    .then(() => {
        bootstrap.Modal.getInstance(document.getElementById('modalEliminar')).hide();
        cargarPeliculas();
    });
}